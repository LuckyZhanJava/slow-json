package com.lonicera.concurrent.trace;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.lonicera.concurrent.trace.LeakTracerFactory.AbstractLeakTracer;

public class DemoTracer extends AbstractLeakTracer {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		DemoTracer tracer = new DemoTracer();
		tracer = LeakTracerFactory.trace(tracer, 10000, TimeUnit.MILLISECONDS);
//		tracer.close();
		Thread.sleep(1000 * 50);
	}

	@Override
	public void close() throws IOException {
		
	}

}
