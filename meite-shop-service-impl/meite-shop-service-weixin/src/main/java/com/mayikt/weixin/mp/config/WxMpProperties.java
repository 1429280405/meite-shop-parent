package com.mayikt.weixin.mp.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.mayikt.weixin.mp.utils.JsonUtils;

import lombok.Data;

/**
 * wechat mp properties
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Data
@ConfigurationProperties(prefix = "wx.mp")
public class WxMpProperties {
	private List<MpConfig> configs;

	@Data
	public static class MpConfig {
		/**
		 * 设置微信公众号的appid
		 */
		private String appId;

		/**
		 * 设置微信公众号的app secret
		 */
		private String secret;

		/**
		 * 设置微信公众号的token
		 */
		private String token;

		/**
		 * 设置微信公众号的EncodingAESKey
		 */
		private String aesKey;


		public String getAppId() {
			return appId;
		}

		public void setAppId(String appId) {
			this.appId = appId;
		}

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getAesKey() {
			return aesKey;
		}

		public void setAesKey(String aesKey) {
			this.aesKey = aesKey;
		}
	}

	public List<MpConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(List<MpConfig> configs) {
		this.configs = configs;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
