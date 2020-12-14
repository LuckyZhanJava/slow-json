package com.lonicera.concurrent.trace;

import java.io.Closeable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public final class LeakTracerFactory {
	private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(16);

	private LeakTracerFactory() {

	}

	@SuppressWarnings("unchecked")
	public static <T extends AbstractLeakTracer> T trace(final T tracer, long maxDelay, TimeUnit unit) {
		final ScheduledFuture<?> future = scheduledExecutorService.schedule(new Runnable() {
			@Override
			public void run() {
				tracer.report();
			}
		}, maxDelay, unit);
		Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tracer.getClass());
        enhancer.setCallback(new MethodInterceptor() {

			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				System.out.println(method);
				if(method.getName().equals("close") && method.getParameterCount() == 0) {
					future.cancel(true);
				}
				return proxy.invokeSuper(obj, args);
			}
        	
        });
		return (T)enhancer.create();
	}

	private static interface LeakTracer{
		void report();
	} 

	public static abstract class AbstractLeakTracer implements LeakTracer,Closeable {
		private String traceMessage;
		protected AbstractLeakTracer() {
			Exception e = new RuntimeException();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			traceMessage = sw.toString();
		}

		public final void report() {
			System.err.print(traceMessage);
		}
	}
}
