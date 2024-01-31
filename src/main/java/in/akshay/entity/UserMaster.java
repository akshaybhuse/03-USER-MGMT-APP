package in.akshay.entity;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "USER_MASTER")
@Data
public class UserMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	private String userName;

	private String userMail;

	private Long userMobile;

	private Character userGender;

	private LocalDate userDob;

	private Long Ssn;

	private String password;

	private String activeStatus;

	@CreationTimestamp
	@Column(insertable = false)
	private LocalDate createdDate;

	@UpdateTimestamp
	@Column(updatable = false)
	private LocalDate updatedDate;

	private String createdBy;

	private String updatedBy;
}
