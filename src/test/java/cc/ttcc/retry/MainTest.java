package cc.ttcc.retry;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import cc.ttcc.retry.common.RetryEntity;
import cc.ttcc.retry.service.RetryAbstractService;
import cc.ttcc.retry.service.RetryDataPersistService;
import cc.ttcc.retry.service.RetryDataPersistTestServiceImpl;
import cc.ttcc.retry.service.RetryRollBackTestService;

import com.alibaba.fastjson.JSON;

public class MainTest {

	@Before
	public void before() {
		System.out.println(123);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws InterruptedException {
		// 必选服务，用于重试回调，执行具体的重试逻辑，须实现RetryAbstractService服务
		RetryAbstractService retryBackService = new RetryRollBackTestService();

		// 可选服务，用于数据持久化以便重启恢复，须实现RetryDataPersistService服务
		RetryDataPersistService retryDataPersistService = new RetryDataPersistTestServiceImpl();

		RetryTask retryTask = new RetryTask(retryBackService, retryDataPersistService);
		// 功能测试
		normalTest(retryTask);
		// 性能测试
		// xnTest(retryTask);
		// 阻塞测试
		// pfTest(retryTask);
	}

	public static void normalTest(RetryTask retryTask) throws InterruptedException {
		Thread.sleep(1000);
		RetryEntity zhifubao = new RetryEntity("优惠券", "rollbackCoupons", "coupons");
		retryTask.add(zhifubao);

		Thread.sleep(1000);
		RetryEntity yue = new RetryEntity("余额", "rollbackBalance", null, new int[] { -2, 4, 6, 8 });
		retryTask.add(yue);

		Thread.sleep(1000);
		Map<String, String> params3 = new HashMap<String, String>();
		params3.put("id", "1234");
		params3.put("userId", "4321");
		RetryEntity tttt = new RetryEntity("微信", "rollbackBalance", JSON.toJSONString(params3),
				new int[] { 2, 4, 6, 8 });
		retryTask.add(tttt);
	}

	public static void xnTest(RetryTask retryTask) throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			Thread.sleep(200);
			Map<String, String> params2 = new HashMap<String, String>();
			params2.put("id", "8888889001943");
			params2.put("webuserId", "1443");
			RetryEntity yue = new RetryEntity("test" + i, "rollbackBalance", JSON.toJSONString(params2), new int[] { 2,
					4, 6, 8 });
			retryTask.add(yue);
		}
	}

	public static void pfTest(RetryTask retryTask) throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			Thread.sleep(1500);
			RetryEntity zhifubao = new RetryEntity("优惠券" + i, "rollbackCoupons", null, new int[] { 1 });
			retryTask.add(zhifubao);
		}
	}
}
