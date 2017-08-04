package it.ctalpa.planning.security;

/**
 * Utility class for Spring Security.
 * Created by c.talpa on 22/05/2017.
 */
public final class SecurityUtils {

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */

    public static String getCurrentUserLogin() {

        String userName=null;
        /*
        SecurityContext securityContext= SecurityContextHolder.getContext();
        Authentication authentication= securityContext.getAuthentication();


        if(authentication!=null && authentication.getPrincipal()!= null){
            if(authentication.getPrincipal() instanceof UserDetails){
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName=springSecurityUser.getUsername();
            }else{
                if(authentication.getPrincipal() instanceof String){
                    userName=(String) authentication.getPrincipal();
                }
            }
        }*/

        return userName;
    }

}
