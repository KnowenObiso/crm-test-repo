package TurnQuest.view.Base;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class SessionExpiredFilter implements Filter {
    
                private FilterConfig _filterConfig = null;
                
                public void init(FilterConfig filterConfig) throws ServletException {
                    _filterConfig = filterConfig;
                }
             
                public void destroy() {
                    _filterConfig = null;
                }
             
              public void doFilter( ServletRequest request, ServletResponse response,
                  FilterChain filterChain ) {
 
                 try{
                        ISession.doFilter(request, response, filterChain);
                     } catch (Exception e) {
                        GlobalCC.EXCEPTIONREPORTING(e);
                     }
                }
                
            } 

