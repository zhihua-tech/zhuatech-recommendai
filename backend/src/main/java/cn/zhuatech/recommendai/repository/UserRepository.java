/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.recommendai.repository; import cn.zhuatech.recommendai.model.UserAccount; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional;
public interface UserRepository extends JpaRepository<UserAccount,Long>{Optional<UserAccount> findByUsername(String username);}
