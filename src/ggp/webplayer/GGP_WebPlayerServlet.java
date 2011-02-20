package ggp.webplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import javax.servlet.http.*;

import com.google.appengine.repackaged.org.json.JSONObject;

import player.gamer.statemachine.StateMachineGamer;
import player.gamer.statemachine.simple.SimpleSearchLightGamer;
import player.request.factory.RequestFactory;
import player.request.grammar.Request;
import util.game.Game;
import util.gdl.grammar.GdlPool;
import util.match.Match;

@SuppressWarnings("serial")
public class GGP_WebPlayerServlet extends HttpServlet {
    private final int maxPlayClock = 1000;
    
    @Override
    public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {  
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Age", "86400");    
    }    
    
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Age", "86400");

        String in = req.getRequestURI();
        in = in.substring(1);
        in = URLDecoder.decode(in, "UTF-8");
        in = in.replace((char)13, ' ');
        
        resp.getWriter().println("GET not supported. Sorry!");
        resp.getWriter().println("Meanwhile, you wrote: [" + in + "]");
        resp.getWriter().println("Speaking to subdomain: [" + req.getServerName() + "]");
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Age", "86400");        

        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        int contentLength = Integer.parseInt(req.getHeader("Content-Length").trim());
        StringBuilder theInput = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            theInput.append((char)br.read());
        }
        String in = theInput.toString().trim();

        String response = handleMessage(req.getServerName(), in);

        resp.setStatus(200);
        resp.setContentLength(response.length());        
        resp.getWriter().println(response);
        resp.getWriter().close();
    }
    
    public String handleMessage(String host, String in) {
        StateMachineGamer theGamer = new SimpleSearchLightGamer();
        
        try {
            Request request = new RequestFactory().create(theGamer, in);
            String matchId = host + "::" + request.getMatchId();
            
            // Load current progress in the match, if available.
            OngoingMatch theOngoingMatch = OngoingMatch.loadOngoingMatch(matchId);
            if (theOngoingMatch != null) {
                String myRole = theOngoingMatch.getMyRole();                
                String theGameJSON = theOngoingMatch.getGameJSON();
                String theMatchJSON = theOngoingMatch.getMatchJSON();
                
                Match theMatch = new Match(theMatchJSON, Game.loadFromJSON(theGameJSON));
                theGamer.setMatch(theMatch);
                theGamer.setRoleName(GdlPool.getProposition(GdlPool.getConstant(myRole)));
                theGamer.resetStateMachine(theMatch.getMostRecentState());
            }
            
            String out = request.process(System.currentTimeMillis());            
            
            if (theOngoingMatch == null) {
                // Construct a new OngoingMatch object if we didn't load one
                // from the datastore.
                Match theMatch = theGamer.getMatch();
                String theGameJSON = theMatch.getGame().serializeToJSON();
                theOngoingMatch = new OngoingMatch(host + "::" + theMatch.getMatchId(), theGameJSON, theGamer.getRoleName().getName().getValue().toString());
                
                // Force the match to have a play clock of at most "maxPlayClock".
                // This ensures that we don't use too much CPU on AppEngine.
                String theMatchJSON = theGamer.getMatch().toJSON();
                JSONObject theModifiedMatchObject = new JSONObject(theMatchJSON);
                if (theModifiedMatchObject.getInt("playClock") > maxPlayClock) {
                    theModifiedMatchObject.put("playClock", maxPlayClock);
                }
                theMatch = new Match(theModifiedMatchObject.toString(), theGamer.getMatch().getGame());
            }
            if (theGamer.getMatch() == null) {
                // Match just ended; we should delete it from our datastore.
                OngoingMatch.deleteOngoingMatch(matchId);
                return out;
            }            
            theOngoingMatch.setMatchJSON(theGamer.getMatch().toJSON());
            theOngoingMatch.save();
            
            return out;
        } catch (Exception e) {
            return "ERROR: " + e;
        }        
    }
}
