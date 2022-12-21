import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet
{
    String message = null; //定义message变量
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        if(username.length()==0)
        {
            HttpSession session = req.getSession();
            message = "用户名不能为空";
            session.setAttribute("message",message);
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
        String password = req.getParameter("password");
        int[] type = new int[2];
        type[0] = -1;
        type[1] = -1;
        try {
            if (LoginCheck(username, password, type))
            {
                String role = null;
                if (type[1] == 0)
                    role = "顾客";
                else if (type[1] == 1)
                    role = "商家";
                Cookie loginCookie = new Cookie("LoginInfo", "True|" + username + "|" + role);
                loginCookie.setMaxAge(60 * 60 * 24 * 30);
                resp.addCookie(loginCookie);
                resp.sendRedirect("main.jsp");
            }
            else
            {
                if(type[0] == 1)
                {
                    HttpSession session = req.getSession();
                    message = "密码错误";
                    session.setAttribute("message",message);
                }
                else if (type[0] == 2)
                {
                    HttpSession session = req.getSession();
                    message = "用户名不存在";
                    session.setAttribute("message",message);
                }
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean LoginCheck(String username, String password, int[] type) throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;database=电商数据;encrypt=false";
        Connection conn = DriverManager.getConnection(url, "eshop", "93576881");
        Statement st = conn.createStatement();

        String sql = "select 用户名 from 账号数据 ";
        ResultSet rs = st.executeQuery(sql);
        List<String> uid = new ArrayList<>();
        while (rs.next())
        {
            uid.add(rs.getString("用户名"));
        }
        Object[] USERNAME = uid.toArray();

        sql = "select 密码 from 账号数据 ";
        rs = st.executeQuery(sql);
        List<String> upwd = new ArrayList<>();
        while (rs.next())
        {
            upwd.add(rs.getString("密码"));
        }
        Object[] PASSWORD = upwd.toArray();

        sql = "select 用户类型 from 账号数据 ";
        rs = st.executeQuery(sql);
        List<String> status = new ArrayList<>();
        while (rs.next())
        {
            status.add(rs.getString("用户类型"));
        }
        Object[] STATUS = status.toArray();
        for(int i=0; i<USERNAME.length;i++)
        {
            if(username.equals(USERNAME[i]))
                if(password.equals(PASSWORD[i]))
                {
                    type[0] = 0;
                    if(STATUS[i].equals("买家"))
                        type[1]=0;
                    else
                        type[1]=1;
                    return true;
                }
                else {
                    type[0] = 1;
                    return false;
                }
        }
        if (type[0]==-1)
        {
            type[0] = 2;
            return false;
        }
        return false;
    }
}
