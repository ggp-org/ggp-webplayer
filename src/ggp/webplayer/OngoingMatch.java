package ggp.webplayer;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.*;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class OngoingMatch {
    @PrimaryKey @Persistent private String matchId;

    @Persistent private Text matchJSON;
    @Persistent private Text gameJSON;
    @Persistent private String myRole;
    
    public OngoingMatch(String matchId, String gameJSON, String myRole) {
        this.myRole = myRole;
        this.matchId = matchId;
        this.gameJSON = new Text(gameJSON);
    }
    
    public String getMatchJSON() {
        return matchJSON.getValue();
    }
        
    public String getGameJSON() {
        return gameJSON.getValue();
    }
    
    public String getMyRole() {
        return myRole;
    }

    public String getMatchId() {
        return matchId;
    }
    
    public void setMatchJSON(String theJSON) {
        this.matchJSON = new Text(theJSON);
    }
    
    public void save() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        pm.makePersistent(this);
        pm.close();
    }
    
    /* Static methods */
    
    public static OngoingMatch loadOngoingMatch(String matchId) {
        OngoingMatch theMatch = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            theMatch = pm.detachCopy(pm.getObjectById(OngoingMatch.class, matchId));
        } catch(JDOObjectNotFoundException e) {
            ;
        } finally {
            pm.close();
        }
        return theMatch;
    }
    
    public static void deleteOngoingMatch(String matchId) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.deletePersistent(pm.getObjectById(OngoingMatch.class, matchId));
        } catch(JDOObjectNotFoundException e) {
            ;
        } finally {
            pm.close();
        }
    }    
}