package edu.sandau.validate.code;

import edu.sandau.validate.code.processor.ValidateCodeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ValidateCodeProcessorHolder {
	@Autowired
	private Map<String, ValidateCodeProcessor> validateCodeProcessors;

	public ValidateCodeProcessor findValidateCodeProcessor(String type) {
		String name = type.toLowerCase().concat("CodeProcessor");
		ValidateCodeProcessor processor = validateCodeProcessors.get(name);
		if (processor == null) {
			log.error("验证码处理器" + name + "不存在");
		}
		return processor;
	}

}
