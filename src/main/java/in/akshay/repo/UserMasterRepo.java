package in.akshay.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.akshay.entity.UserMaster;

public interface UserMasterRepo extends JpaRepository<UserMaster, Integer> {
	
public UserMaster findByUserMailAndPassword(String email, String Password);

public UserMaster findByUserMail(String email);

}
