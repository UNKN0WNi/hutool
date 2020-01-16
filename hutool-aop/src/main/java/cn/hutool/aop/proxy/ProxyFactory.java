package cn.hutool.aop.proxy;

import java.io.Serializable;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.core.util.ReflectUtil;

/**
 * 代理工厂<br>
 * 根据用户引入代理库的不同，产生不同的代理对象
 * 
 * @author looly
 *
 */
public abstract class ProxyFactory implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 创建代理
	 *
	 * @param <T> 代理对象类型
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 * @return 代理对象
	 */
	public abstract <T> T proxy(T target, Aspect aspect);
	
	/**
	 * 根据用户引入Cglib与否自动创建代理对象
	 * 
	 * @param <T> 切面对象类型
	 * @param target 目标对象
	 * @param aspectClass 切面对象类
	 * @return 代理对象
	 */
	public static <T> T createProxy(T target, Class<? extends Aspect> aspectClass){
		//反射获取一个传入切面类对象
		return createProxy(target, ReflectUtil.newInstance(aspectClass));
	}

	/**
	 * 根据用户引入Cglib与否自动创建代理对象
	 * 
	 * @param <T> 切面对象类型
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 * @return 代理对象
	 */
	public static <T> T createProxy(T target, Aspect aspect) {
		//proxy调用返回的具体代理工厂的proxy，即jdk/cglib的不同代理实现
		return create().proxy(target, aspect);
	}

	/**
	 * 根据用户引入Cglib与否创建代理工厂
	 * 
	 * @return 代理工厂
	 */
	public static ProxyFactory create() {
		try {
			//如果引入了cglib返回cglib代理工厂
			return new CglibProxyFactory();
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		return new JdkProxyFactory();
	}
}
