package kin.devplatform.network.model;
/*
 * Kin Ecosystem
 * Apis for client to server interaction
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


import static kin.devplatform.core.util.StringUtil.toIndentedString;

import com.google.gson.annotations.SerializedName;

/**
 * user properties
 */
public class UserProperties {

	@SerializedName("wallet_address")
	private String walletAddress = null;

	public UserProperties walletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
		return this;
	}


	/**
	 * user public address
	 *
	 * @return walletAddress
	 **/
	public String getWalletAddress() {
		return walletAddress;
	}

	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserProperties userProperties = (UserProperties) o;
		return this.walletAddress.equals(userProperties.walletAddress);
	}

	@Override
	public int hashCode() {
		return walletAddress.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UserProperties {\n");

		sb.append("    walletAddress: ").append(toIndentedString(walletAddress)).append("\n");
		sb.append("}");
		return sb.toString();
	}
}



