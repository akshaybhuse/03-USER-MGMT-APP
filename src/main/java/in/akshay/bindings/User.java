package in.akshay.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
	
	private String userName;
	private String userMail;
	private Long userMobile;
	private char userGender;
	private LocalDate userDob;
	private long Ssn;

}
