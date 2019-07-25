package xin.wjtree.qmq;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;

public class Test {

	public static void main(String[] args) throws IllegalAccessException {
		Son son = new Son();
		son.setSalary(111111111.0D);
		son.setBirtyDay(LocalDate.now());
		son.setId(0L);
		son.setName("张三");
		son.setAge(11);
		son.setJob("后端开发");
		son.setDuty("高级程序员");
		System.out.println(son);

		Class<?> aClass = son.getClass();

		while (aClass != null){
			Field[] declaredFields = aClass.getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				System.out.println(Arrays.toString(field.getDeclaredAnnotations()));
				System.out.println(field.getName() + " "  + field.getType() + " " + field.get(son));
				System.out.println();
			}
			aClass = aClass.getSuperclass();
		}


	}

}
