package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import models.validators.MessageValidator;
import util.DBUtil;

import java.util.List;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String _token = (String)request.getParameter("_token");
		//CSRF対策
		if(_token != null && _token.equals(request.getSession().getId())){
	        EntityManager em =DBUtil.createEntityManager();

	        Message m = new Message();

	        String title = request.getParameter("title");
	        m.setTitle(title);

	        String content = request.getParameter("content");
	        m.setContent(content);

	        //↓現在日時の情報をもつ日付型のオブジェクト
	        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
	        m.setCreated_at(currentTime);
	        m.setUpdated_at(currentTime);

	        //バリデーションを実行してエラーがあったら新規登録のフォームに戻る
	        List<String>errors = MessageValidator.validate(m);
	        if(errors.size() > 0){
	            em.close();

	            //フォームに初期値を設定、さらにエラーメッセージを送る、リクエストスコープへ格納
	            request.setAttribute("_token", request.getSession().getId());
	            request.setAttribute("message", m);
	            request.setAttribute("errors", errors);

	            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/messages/new.jsp");
	            rd.forward(request, response);
	        }else{

	            //結果それ以外をデータベースに保存
	        em.getTransaction().begin();
	        em.persist(m);
	        em.getTransaction().commit();
	        request.getSession().setAttribute("flush", "登録が完了しました");//フラッシュメッセージをセッションスコープへ保存
	        em.close();

	        response.sendRedirect(request.getContextPath() + "/index");
	        }
	}
	}
}
