package xin.wjtree.qmq;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class Son extends Father {

	@Ignore
	private Double salary;

	private LocalDate birtyDay;

}
