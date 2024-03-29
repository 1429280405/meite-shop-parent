package com.mayikt.pay.mapper;

import org.apache.ibatis.annotations.*;

import com.mayikt.pay.mapper.entity.PaymentTransactionEntity;

public interface PaymentTransactionMapper {
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("INSERT INTO `payment_transaction` VALUES (null, #{payAmount}, '0', #{userId}, #{orderId}, null, null, now(), null, now(),null,#{paymentId});")
	public int insertPaymentTransaction(PaymentTransactionEntity paymentTransactionEntity);

	@Select("SELECT ID AS ID ,pay_Amount AS payAmount,payment_Status AS paymentStatus,user_ID AS userId, order_Id AS orderId , created_Time as createdTime ,partypay_Id as partyPayId , payment_Id as paymentId FROM payment_transaction WHERE ID=#{id};")
	public PaymentTransactionEntity selectById(Long id);

	@Select("SELECT ID AS ID ,pay_Amount AS payAmount,payment_Status AS paymentStatus,user_ID AS userId, order_Id AS orderId , created_Time as createdTime ,partypay_Id as partyPayId , payment_Id as paymentId FROM payment_transaction WHERE PAYMENT_ID=#{paymentId};")
	public PaymentTransactionEntity selectByPaymentId(String paymentId);

	@Update("update payment_transaction SET PAYMENT_STATUS=#{paymentStatus}  WHERE PAYMENT_ID=#{paymentId};")
	public int updatePaymentStatus(@Param("paymentStatus") String paymentStatus, @Param("paymentId") String paymentId);
	
}
