package xin.wjtree.qmq;

import lombok.Data;
import xin.wjtree.qmq.internal.QmqIgnore;

@Data
public class Father {

	private Long id;

	@QmqIgnore
	private String name;

	private Integer age;

	private String job;

	private String duty;

}
