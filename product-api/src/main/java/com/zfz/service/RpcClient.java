package com.zfz.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

import com.zfz.service.bean.Product;

public class RpcClient {
	public static void main(String[] args) {
		IProductService productService = (IProductService)rpc(IProductService.class);
		//调用queryById方法时debug才会进入rpc方法；rpc方法是静态方法
		Product product = productService.queryById(100);
		System.out.println(product);
	}

	public  static Object  rpc(final Class clazz) {
		return Proxy.newProxyInstance(clazz.getClassLoader(), 
				new Class[] {clazz}, new InvocationHandler() {
					
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Socket socket = new Socket("127.0.0.1", 8888);
						String apiClassName = clazz.getName();
						String methodName = method.getName();
						Class[] parameterTypes = method.getParameterTypes();
						//1.客户端发送需要调用的类名，方法名，参数类型，参数给服务端
						ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
						objectOutputStream.writeUTF(apiClassName);
						objectOutputStream.writeUTF(methodName);
						objectOutputStream.writeObject(parameterTypes);
						objectOutputStream.writeObject(args);
						objectOutputStream.flush();
						//2.获取服务端发送来的对象
						ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
						Object o = objectInputStream.readObject();
						objectInputStream.close();
						objectOutputStream.close();
						socket.close();
						return o;
					}
				});
	}
}
