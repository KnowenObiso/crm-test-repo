package TurnQuest.view.Base;


import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;


public class SysDAO {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    public SysDAO() {
        super();
    }
    public List<sys> findUserSystems() {
        return ISession.findUserActiveSystems(session);
    }

    public List<sys> findSystems() {
        return ISession.findUserActiveSystems(session);
    }
}
