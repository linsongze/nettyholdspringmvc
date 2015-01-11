package org.imlsz.nettyspringmvc.netty;

import javax.servlet.ServletException;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpChunkAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;


public class DispatcherServletChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final DispatcherServlet dispatcherServlet;

    public DispatcherServletChannelInitializer() throws ServletException {

    	MockServletContext servletContext = new MockServletContext();
    	MockServletConfig servletConfig = new MockServletConfig(servletContext);
        servletConfig.addInitParameter("contextConfigLocation","classpath:/META-INF/spring/root-context.xml");
        servletContext.addInitParameter("contextConfigLocation","classpath:/META-INF/spring/root-context.xml");

    	//AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
        XmlWebApplicationContext wac = new XmlWebApplicationContext();

        //ClassPathXmlApplicationContext wac = new ClassPathXmlApplicationContext();
		wac.setServletContext(servletContext);
		wac.setServletConfig(servletConfig);
        wac.setConfigLocation("classpath:/servlet-context.xml");
    	//wac.register(WebConfig.class);
    	wac.refresh();

    	this.dispatcherServlet = new DispatcherServlet(wac);
    	this.dispatcherServlet.init(servletConfig);
	}

    @Override
    public void initChannel(SocketChannel channel) throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = channel.pipeline();

        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //pipeline.addLast("ssl", new SslHandler(engine));

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        pipeline.addLast("handler", new ServletNettyHandler(this.dispatcherServlet));
    }


    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackages="org.springframework.sandbox.mvc")
    static class WebConfig extends WebMvcConfigurerAdapter {
    }

}
