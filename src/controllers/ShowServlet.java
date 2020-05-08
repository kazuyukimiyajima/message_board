//データベースからデータ取得のメソッド DTO
package controllers;

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


/**
 * Servlet implementation class ShowServlet
 */
@WebServlet("/show")
public class ShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManager em = DBUtil.createEntityManager();

		//該当のIDメッセージ１件のみをデータベースから取得   request.getParameter()←クエリパラメータの取得 どのデータもSting型で取得する
		Message m = em.find(Message.class, Integer.parseInt(request.getParameter("id")));
                                           //↑Interger.parseInt() String型の１を整数の１に変える役割
		em.close();

		//メッセージデータをリクエストスコープにセットしてshow.jspを呼びだす
	    request.setAttribute("message", m);

	    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/messages/show.jsp");
	    rd.forward(request, response);

	}

}
