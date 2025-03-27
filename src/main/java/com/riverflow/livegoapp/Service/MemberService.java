package com.riverflow.livegoapp.Service;

import com.riverflow.livegoapp.Entity.Member;
import com.riverflow.livegoapp.Jwt.JwtUtil;
import com.riverflow.livegoapp.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Optional;


@Service
public class MemberService {

    public String imagePath = "fileUpload/member/image";

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    protected JwtUtil jwtUtil;


    public Member getMemberByUid(String uid) {
        return memberRepository.findByUid(uid);

    }

    public Member getMemberByZid(String zid) {
        return memberRepository.findByZid(zid);
    }

    /*
     *@  로그인
     * params : uid,upass
     * return : {status: -> message,success.fail} access_token
     */
    @Transactional
    public HashMap<String, Object> memberLogin(String uid, String pass) {

        HashMap<String, Object> result = new HashMap<String, Object>();

        if (!uid.isEmpty() && !pass.isEmpty()) {


            Member findMember = memberRepository.findByUid(uid);

            if (findMember == null) {
                result.put("code", "wrong");
                result.put("status", "message");
            } else {
                if (!findMember.getRole().equals("admin")) {
                    if (!findMember.getAuth().equals("yes")) {
                        result.put("code", "notauth");
                        result.put("status", "message");
                        return result;
                    }
                    if (findMember.getUout().equals("yes")) {
                        result.put("code", "uout");
                        result.put("status", "message");
                        return result;
                    }
                }


                PasswordEncoder encoder = new BCryptPasswordEncoder();
                if (encoder.matches(pass, findMember.getPasswd())) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(findMember.getUid());
                    String token = jwtUtil.generateToken(userDetails);
                    result.put("access_token", token);
                    result.put("memberInfo", findMember);
                    result.put("status", "success");
                } else {
                    result.put("code", "wrong");
                    result.put("status", "message");
                }
            }
        } else {
            result.put("status", "fail");
        }

        return result;
    }


    private void memberUpdateActive(Long id, HashMap<String, Object> params) {
        Member isMember = memberRepository.getById(id);
        if (isMember != null) {
            String img = isMember.getImg();
            String emailSend = isMember.getEmailSend();
            String password = isMember.getPasswd();
            String auth = isMember.getAuth();
            String uout = isMember.getUout();
            String accessToken = isMember.getAccessToken();
            String refreshToken = isMember.getRefreshToken();
            String zid = isMember.getZid();
            int expires  = isMember.getExpires();

            String updateType = (String) params.get("updateType");

            switch (updateType) {
                case "img":
                    img = (String) params.get("img");
                    break;
                case "emailSend":
                    emailSend = (String) params.get("emailSend");
                    break;
                case "password":
                    password = (String) params.get("password");
                    break;
                case "auth":
                    auth = (String) params.get("auth");
                    break;
                case "out":
                    uout = "yes";
                    break;
                case "zoom":
                    accessToken = (String) params.get("accessToken");
                    refreshToken = (String) params.get("refreshToken");
                    if(params.get("expires")!=null) {
                        expires  = (int) params.get("expires");
                    } else expires  = 1;

                    break;
            }
            Member member = Member.builder()
                    .uid(isMember.getUid())
                    .passwd(password)
                    .auth(auth)
                    .role(isMember.getRole())
                    .name(isMember.getName())
                    .email(isMember.getEmail())
                    .emailSend(emailSend)
                    .img(img)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expires(expires)
                    .zid(zid)
                    .uout(uout)
                    .createAt(isMember.getCreateAt())
                    .actType("update")
                    .actId(id)
                    .build();

            memberRepository.save(member);
        }
    }

    /**
     * @param
     * @ 줌으로 로그인
     */
    public HashMap<String, Object> loginByZoom(Member findMember,HashMap<String, Object> params) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        if (!findMember.getRole().equals("admin")) {
            if (!findMember.getAuth().equals("yes")) {
                result.put("code", "notauth");
                result.put("status", "message");
                return result;
            }
            if (findMember.getUout().equals("yes")) {
                result.put("code", "uout");
                result.put("status", "message");
                return result;
            }
        }

        memberUpdateActive(findMember.getId(),params);

        UserDetails userDetails = userDetailsService.loadUserByUsername(findMember.getUid());
        String token = jwtUtil.generateToken(userDetails);
        result.put("access_token", token);
        result.put("memberInfo", findMember);
        result.put("status", "success");
        return result;
    }

    /**
     * @param zoomResult
     * @줌으로 회원가입
     */
    public HashMap<String, Object> insertMemberByZoom(HashMap<String, Object> zoomResult) {

        HashMap<String, Object> result = new HashMap<String, Object>();

        String zid = (String) zoomResult.get("zid");
        String uid = (String) zoomResult.get("zid");
        String password = (String) zoomResult.get("zid");
        String name =  (String) zoomResult.get("name");
        String email =  (String) zoomResult.get("email");
        String accessToken = (String) zoomResult.get("accessToken");
        String refreshToken = (String) zoomResult.get("refreshToken");
        int expires  = (int) zoomResult.get("expires");


        Member member = Member.builder()
                .uid(uid)
                .passwd(password)
                .auth("yes")
                .role("user")
                .name(name)
                .email(email)
                .emailSend("no")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expires(expires)
                .zid(zid)
                .uout("no")
                .actType("insert")
                .build();

        Member findMember = memberRepository.save(member);

        UserDetails userDetails = userDetailsService.loadUserByUsername(findMember.getUid());
        String token = jwtUtil.generateToken(userDetails);
        result.put("access_token", token);
        result.put("memberInfo", findMember);
        result.put("status", "success");

        return result;
    }
}
