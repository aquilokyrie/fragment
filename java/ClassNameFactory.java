package me.test.charge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import me.test.charge.process.ChargeProcess;
import me.test.charge.request.ChargeRequest;

public class ClassNameFactory {

	public static ChargeProcess getProcess(String operationCode) throws BusyinessException{
		
		ChargeProcess process = null;
		
		try {
			process = (ChargeProcess) Class.forName("me.test.charge.process.ChargeProcess" + operationCode).newInstance();
			
		} catch (ClassNotFoundException e) {
			throw new BusyinessException("无效的操作标识");
		} catch (InstantiationException e) {
			throw new BusyinessException("服务器系统错误");
		} catch (IllegalAccessException e) {
			throw new BusyinessException("服务器系统错误");
		}
		
//		System.out.println("INFO[Factory] 生产了process : " + process);
		return process;
	}
	
	public static ChargeRequest getRequest(String operationCode,Map<String,String> params) throws BusyinessException{
		
		ChargeRequest request = null;
		
		try {
			request = (ChargeRequest) Class.forName("me.test.charge.request.ChargeRequest" + operationCode).newInstance();
			
			Method[] methods = request.getClass().getDeclaredMethods();

			for(Iterator<String> it = params.keySet().iterator() ; it.hasNext() ; ){
				String item = it.next();
				//使用反射设置传值类当中的属性
				for (Method method : methods){
					if(item.equalsIgnoreCase(method.getName().substring(3)) && method.getName().startsWith("set")){
						method.invoke(request, params.get(item));
					}
				}
			}
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new BusyinessException("服务器系统错误");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new BusyinessException("服务器系统错误");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new BusyinessException("无效的参数项");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new BusyinessException("无效的参数项");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new BusyinessException("无效的参数项");
		}
		
//		System.out.println("INFO[Factory] 生产了request : " + request);
		return request;
	}
	
}
