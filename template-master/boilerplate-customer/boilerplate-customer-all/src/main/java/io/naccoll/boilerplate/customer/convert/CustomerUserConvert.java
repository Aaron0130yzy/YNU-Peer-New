package io.naccoll.boilerplate.customer.convert;

import java.util.List;

import cn.hutool.core.util.DesensitizedUtil;
import io.naccoll.boilerplate.core.security.service.DataSecurityService;
import io.naccoll.boilerplate.customer.dto.CustomerUserBindQueryCondition;
import io.naccoll.boilerplate.customer.dto.CustomerUserDto;
import io.naccoll.boilerplate.customer.dto.CustomerUserWithBindDto;
import io.naccoll.boilerplate.customer.model.CustomerUserBindPo;
import io.naccoll.boilerplate.customer.model.CustomerUserPo;
import io.naccoll.boilerplate.customer.service.CustomerUserBindQueryService;
import io.naccoll.boilerplate.oss.OssServiceHelper;
import jakarta.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author NaccOll
 */
@Component
public class CustomerUserConvert {

	@Resource
	private DataSecurityService dataSecurityService;

	@Resource
	private OssServiceHelper ossServiceHelper;

	@Resource
	private CustomerUserBindConvert customerUserBindConvert;

	@Resource
	private CustomerUserBindQueryService customerUserQueryService;

	public CustomerUserWithBindDto convertCustomerUserWithBindDto(CustomerUserPo po) {
		CustomerUserDto dto = convertCustomerUserDtoList(List.of(po)).get(0);
		CustomerUserWithBindDto result = new CustomerUserWithBindDto();
		BeanUtils.copyProperties(dto, result);
		CustomerUserBindQueryCondition condition = new CustomerUserBindQueryCondition();
		condition.setCustomerUserId(po.getId());
		List<CustomerUserBindPo> binds = customerUserQueryService.findAll(condition);
		result.setBinds(customerUserBindConvert.convertCustomerUserBindDtoList(binds));
		return result;
	}

	public CustomerUserDto convertCustomerUserDto(CustomerUserPo po) {
		return convertCustomerUserDtoList(List.of(po)).get(0);
	}

	public List<CustomerUserDto> convertCustomerUserDtoList(List<CustomerUserPo> list) {
		List<CustomerUserDto> dtos = list.stream().map(po -> {
			CustomerUserDto dto = new CustomerUserDto();
			BeanUtils.copyProperties(po, dto);
			dto.setPassword(null);
			String tel = dataSecurityService.decryptStr(po.getTel());
			if (StringUtils.hasText(tel)) {
				dto.setTel(DesensitizedUtil.mobilePhone(tel));
			}
			if (StringUtils.hasText(po.getEmail())) {
				dto.setEmail(DesensitizedUtil.email(po.getEmail()));
			}
			if (StringUtils.hasText(po.getAvatar())) {
				dto.setAvatar(ossServiceHelper.parseUrl(po.getAvatar()));
			}
			return dto;
		}).toList();
		return dtos;
	}

	public Page<CustomerUserDto> convertCustomerUserDtoPage(Page<CustomerUserPo> page) {
		List<CustomerUserDto> list = convertCustomerUserDtoList(page.getContent());
		return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
	}

}
