package com.muses.avancier.repository;

import org.springframework.data.repository.CrudRepository;

import com.muses.avancier.model.Activity;
import com.muses.avancier.model.WxUserResend;

/**
 * 弹幕重发记录
 * @author kit@muses.cc
 * @since 2017-4-19
 */
public interface WxUserResendRepository extends CrudRepository<WxUserResend, Long> {
	
	/**
	 * 最回下一个待重发的记录
	 * @return
	 */
	WxUserResend findTopByResendedFalseOrderByIdAsc();
    
    /**
     * 删除指定活动的所有未重发记录（清空重发）
     * @param act
     * @return
     */
    void deleteByActivityAndResendedFalse(Activity act);
    
    /**
     * 删除所有已重发过的记录（垃圾清理，保持数据表较少记录）
     * @return
     */
    void deleteByResendedTrue();
    
}
