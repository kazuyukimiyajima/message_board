package controllers; //update更新処理のサーブレット

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import util.DBUtil;


@WebServlet("/edit")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public EditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    EntityManager em = DBUtil.createEntityManager();

	    //該当のIDのメッセージ１件のみをデータベースから取得
	    Message m = em.find(Message.class, Integer.parseInt(request.getParameter("id")));

	    em.close();

	    //メッセージ情報とセッションIDをリクエストスコープへ登録
	    request.setAttribute("message", m);
	    request.setAttribute("_token", request.getSession().getId());
	    
	    //メッセージIDをセッションスコープへ登録
	    request.getSession().setAttribute("message_id", m.getId());
	    

	    RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/messages/edit.jsp");
	    rd.forward(request, response);
	}

}
