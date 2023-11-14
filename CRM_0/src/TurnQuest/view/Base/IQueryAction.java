package TurnQuest.view.Base;
/*author: Dancan Kavagi*/
import java.sql.ResultSet;

public interface IQueryAction {
	public void fetch(ResultSet rs,Object o); 
}
