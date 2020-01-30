## Flux简单源码解析

```java
Flux.just("2","1","3")
                .subscribe(System.out::println);
```


just的内部实现是：返回一个FluxArray类型的实例，内部成员变量final T[] array;来参访参数

1. reactor.core.publisher.Flux#subscribe(java.util.function.Consumer<? super T>)

2. subscribeWith(new LambdaSubscriber<>(consumer, errorConsumer,
				completeConsumer,
				null,
				initialContext));

3. reactor.core.publisher.Flux#subscribe(org.reactivestreams.Subscriber<? super T>)

4. publisher.subscribe(subscriber);  publisher此时为FluxArray实例，subscriber为用System.out::println构造的LamdbaSubscriber.

5. reactor.core.publisher.FluxArray#subscribe(reactor.core.CoreSubscriber<? super T>, T[])，这里就是调用FluxArray的订阅方法了，CoreSubscriber就是LamdbaSubscriber实例，T[]就是FluxArray的内部保存数据的成员变量

```java
public static <T> void subscribe(CoreSubscriber<? super T> s, T[] array) {
		if (array.length == 0) {
			Operators.complete(s);
			return;
		}
		if (s instanceof ConditionalSubscriber) {
			s.onSubscribe(new ArrayConditionalSubscription<>((ConditionalSubscriber<? super T>) s, array));
		}
		else {
			s.onSubscribe(new ArraySubscription<>(s, array));
		}
	}
```

```
new ArraySubscription<>(s, array)；就是创建了一个subscription的实例，使得消费者可以用于向生产者request数据，就是订阅者向发布者请求数据。
```

6. reactor.core.publisher.LambdaSubscriber#onSubscribe，调用订阅者的onSubscribe方法，

```java
public final void onSubscribe(Subscription s) {
		if (Operators.validate(subscription，  s)) {//subscription为null
			this.subscription = s;
			if (subscriptionConsumer != null) {
				try {
					subscriptionConsumer.accept(s);
				}
				catch (Throwable t) {
					Exceptions.throwIfFatal(t);
					s.cancel();
					onError(t);
				}
			}
			else {
				s.request(Long.MAX_VALUE);//调用s的request方法
			}
		}
	}
```



7. reactor.core.publisher.FluxArray.ArraySubscription#request

```java
public void request(long n) {
			if (Operators.validate(n)) {  // n <= 0 返回 false，其他返回true
				if (Operators.addCap(REQUESTED, this, n) == 0) { //？？？
/*
public static <T> long addCap(AtomicLongFieldUpdater<T> updater, T instance, long toAdd) {
long r, u;
		for (;;) {
			r = updater.get(instance);//获取instance（就是ArraySubscription）内部的requested变量的值；初始化为0，long；；toAdd是Long.MAX_VALUE
			if (r == Long.MAX_VALUE) {
				return Long.MAX_VALUE;
			}
			u = addCap(r, toAdd);  //正常情况下，就是返回r的值
			if (updater.compareAndSet(instance, r, u)) {//把instance的requested成员变量值设置成u
				return r;
			}
		}
}
*/
					if (n == Long.MAX_VALUE) {
						fastPath();
					}
					else {
						slowPath(n);
					}
				}
			}
		}
```



8. reactor.core.publisher.FluxArray.ArraySubscription#fastPath

```java
void fastPath() {
			final T[] a = array;    //FluxArray内部的那个成员变量数组
			final int len = a.length;
			final Subscriber<? super T> s = actual;//订阅者，就是那个lamdbaSubscriber

			for (int i = index; i != len; i++) {  //循环遍历所有的元素
				if (cancelled) {
					return;
				}

				T t = a[i];

				if (t == null) {
					s.onError(new NullPointerException("The " + i + "th array element was null"));
					return;
				}

				s.onNext(t); //onNext()方法
			}
			if (cancelled) {
				return;
			}
			s.onComplete();//调用FluxArray的onComplete方法
		}
```

9. reactor.core.publisher.LambdaSubscriber#onNext

```java
public final void onNext(T x) {
		try {
			if (consumer != null) {
				consumer.accept(x);//这里就是调用那个lamdbaSubscribe的表达式的
			}
		}
		catch (Throwable t) {//有异常就取消消费，然后走onError流程
			Exceptions.throwIfFatal(t);
			this.subscription.cancel();
			onError(t);
		}
	}
```


10. reactor.core.publisher.LambdaSubscriber#onComplete 最后走完成的方法

```java
public final void onComplete() {
		Subscription s = S.getAndSet(this, Operators.cancelledSubscription());
		if (s == Operators.cancelledSubscription()) {
			return;
		}
		if (completeConsumer != null) {
			try {
				completeConsumer.run();
			}
			catch (Throwable t) {
				Exceptions.throwIfFatal(t);
				onError(t);
			}
		}
	}
```

11. 然后 整个流程就走完了，，里面涉及一些具体的判断的算法，得仔细的看看，学习，，这才是重点。



