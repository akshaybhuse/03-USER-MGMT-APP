package in.akshay.bindings;

import lombok.Data;

@Data
public class UserActiveAccRequest {

	private String userActiveAccEmail;
	private String userActiveAccTempPwd;
	private String userActiveAccNewPwd;
	private String userActiveAccConfirmPwd;
	
	
}
