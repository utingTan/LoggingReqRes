/**
 * @(#) GwUrlMappingRepo.java
 * 
 * Description: API URL Mapping Repository implements
 *
 * Modify History: 
 * 	v1.00, 2020/05/25, Eason Hsu
 *	 1) First release
 */

package com.tcb.otp.gw.repo.impl;

import org.springframework.stereotype.Repository;

import com.tcb.otp.gw.entity.GwUrlMapping;
import com.tcb.otp.gw.repo.GwUrlMappingRepo;
import com.tcb.otp.repo.impl.OtpRepositoryImpl;

/**
  * 該@Repository註解有兩個目的。
  * 首先，它將類標記為Spring bean，並通過組件掃描使其可發現。
  * 其次，它告訴PersistenceExceptionTranslationPostProcessor捕獲此類拋出的所有平台特定的異常，並將其作為Spring未檢查的異常重新拋出。
 */
@Repository
public class GwUrlMappingRepoImpl extends OtpRepositoryImpl<GwUrlMapping, String> implements GwUrlMappingRepo {

	private static final long serialVersionUID = 4949005110022427150L;

}
