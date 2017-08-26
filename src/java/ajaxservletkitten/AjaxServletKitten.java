/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajaxservletkitten;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author LVD
 */
@WebServlet(name = "AjaxServletKitten", urlPatterns = {"/AjaxServletKitten"})
public class AjaxServletKitten extends HttpServlet {

    public static int spaceCount = 9;
    public static int arr[][] = new int[9][9];
    public static int kitten[] = new int[2];
    public static ArrayList space = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String result = null;
        String comand = request.getParameter("comand");
        if (comand != null) {
            switch (comand) {
                case "getSpace": {
                    space = null;
                    if (space == null) {
                        space = new ArrayList();
                        for (int i = 0; i < spaceCount; i++) {
                            for (int j = 0; j < spaceCount; j++) {
                                arr[i][j] = 0;
                            }
                        }

                        final Random random = new Random();
                        space.add(spaceCount / 2);  //координаты кошки x
                        space.add(spaceCount / 2);  //координаты кошки y
                        space.add(Math.abs(random.nextInt()) % 2 + 1);  //координаты препятсвий x
                        space.add(Math.abs(random.nextInt()) % 5 + 1);  //координаты препятсвий y ...
                        space.add(Math.abs(random.nextInt()) % 5 + 1);
                        space.add(Math.abs(random.nextInt()) % 2 + 6);
                        space.add(Math.abs(random.nextInt()) % 2 + 6);
                        space.add(Math.abs(random.nextInt()) % 5 + 3);
                        space.add(Math.abs(random.nextInt()) % 5 + 3);
                        space.add(Math.abs(random.nextInt()) % 2 + 1);

                        arr[(int) space.get(0)][(int) space.get(1)] = 9;

                        arr[(int) space.get(2)][(int) space.get(3)] = 1;
                        arr[(int) space.get(4)][(int) space.get(5)] = 1;
                        arr[(int) space.get(6)][(int) space.get(7)] = 1;
                        arr[(int) space.get(8)][(int) space.get(9)] = 1;

                        kitten[0] = (int) space.get(0);
                        kitten[1] = (int) space.get(1);
                    }
                    result = "({";
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            if (arr[i][j] != 0) {
                                result += "\"td_" + (i * 9 + j) + "\":\"" + arr[i][j] + "\"";
                                if (i != 8 || j != 8) {
                                    result += ",";
                                }
                            }
                        }
                    }
                    result += "})";
                }
                break;
                case "add": {
                    boolean res = true;
                    String strX = request.getParameter("x");
                    String strY = request.getParameter("y");
                    if (strX != null && strY != null) {
                        int x = Integer.parseInt(strX);
                        int y = Integer.parseInt(strY);
                        if (space != null) {
                            space.add(x);
                            space.add(y);
                            arr[x][y] = 1;
                        } else {
                            res = false;
                        }
                    } else {
                        res = false;
                    }

                    result = "({\"add\":\"" + res + "\"})";
                }
                break;
                case "reset": {
                    space = null;
                    boolean res = true;
                    result = "({\"reset\":\"" + res + "\"})";
                }
                break;
                case "newStep": {
                    boolean res = true;
                    if (space != null) {
                        int sum = 0, finSum = Integer.MAX_VALUE, fx = -2, fy = -2;

                        if (arr[(int) space.get(0) - 1][(int) space.get(1) - 1] != 1) {
                            for (int i = (int) space.get(0) - 1; i >= 0; i--) {
                                for (int j = 0; j < (int) space.get(0) - i; j++) {
                                    if (inInterval(i) && inInterval((int) space.get(1) - 1 - j)) {
                                        if (arr[i][(int) space.get(1) - 1 - j] == 0) {
                                            sum += ((int) space.get(0) - i) * 10;
                                        } else if (arr[i][(int) space.get(1) - 1 - j] == 1) {
                                            sum += (9 - ((int) space.get(0) - i)) * 1000;
                                        }
                                    }
                                }
                            }
                            if (finSum > sum) {
                                fx = -1;
                                fy = -1;
                                finSum = sum;
                            }
                        }
                        sum = 0;

                        if (arr[(int) space.get(0) - 1][(int) space.get(1)] != 1) {
                            for (int i = (int) space.get(0) - 1; i >= 0; i--) {
                                for (int j = 0; j < (int) space.get(0) - i; j++) {
                                    if (inInterval(i) && inInterval((int) space.get(1) + j)) {
                                        if (arr[i][(int) space.get(1) + j] == 0) {
                                            sum += ((int) space.get(0) - i) * 10;
                                        } else if (arr[i][(int) space.get(1) + j] == 1) {
                                            sum += (9 - ((int) space.get(0) - i)) * 1000;
                                        }
                                    }
                                }
                            }
                            if (finSum > sum) {
                                finSum = sum;
                                fx = -1;
                                fy = 0;
                            }
                        }
                        sum = 0;

                        if (arr[(int) space.get(0) - 1][(int) space.get(1) + 1] != 1) {
                            for (int j = (int) space.get(1) + 1; j < 9; j++) {
                                for (int i = 0; i < j - (int) space.get(1); i++) {
                                    if (inInterval(j) && inInterval((int) space.get(0) - 1 - i)) {
                                        if (arr[(int) space.get(0) - 1 - i][j] == 0) {
                                            sum += (j - (int) space.get(1)) * 10;
                                        } else if (arr[(int) space.get(0) - 1 - i][j] == 1) {
                                            sum += (9 - (j - (int) space.get(1))) * 1000;
                                        }
                                    }
                                }
                            }
                            if (finSum > sum) {
                                finSum = sum;
                                fx = -1;
                                fy = 1;
                            }
                        }
                        sum = 0;

                        if (arr[(int) space.get(0)][(int) space.get(1) + 1] != 1) {
                            for (int j = (int) space.get(1) + 1; j < (int) 9; j++) {
                                for (int i = 0; i < j - (int) space.get(1); i++) {
                                    if (inInterval(j) && inInterval((int) space.get(0) + i)) {
                                        if (arr[(int) space.get(0) + i][j] == 0) {
                                            sum += (j - (int) space.get(1)) * 10;
                                        } else if (arr[(int) space.get(0) + i][j] == 1) {
                                            sum += (9 - (j - (int) space.get(1))) * 1000;
                                        }
                                    }
                                }
                            }
                            if (finSum > sum) {
                                finSum = sum;
                                fx = 0;
                                fy = 1;
                            }
                        }
                        sum = 0;

                        if (arr[(int) space.get(0) + 1][(int) space.get(1) + 1] != 1) {
                            for (int i = (int) space.get(0) + 1; i < 9; i++) {
                                for (int j = 0; j < i - (int) space.get(0); j++) {
                                    if (inInterval(i) && inInterval((int) space.get(1) + 1 + j)) {
                                        if (arr[i][(int) space.get(1) + 1 + j] == 0) {
                                            sum += (i - (int) space.get(0)) * 10;
                                        } else if (arr[i][(int) space.get(1) + 1 + j] == 1) {
                                            sum += ((int) 9 - (i - (int) space.get(0))) * 1000;
                                        }
                                    }
                                }
                            }
                            if (finSum > sum) {
                                finSum = sum;
                                fx = 1;
                                fy = 1;
                            }
                        }
                        sum = 0;

                        if (arr[(int) space.get(0) + 1][(int) space.get(1)] != 1) {
                            for (int i = (int) space.get(0) + 1; i < 9; i++) {
                                for (int j = 0; j < i - (int) space.get(0); j++) {
                                    if (inInterval(i) && inInterval((int) space.get(1) - j)) {
                                        if (arr[i][(int) space.get(1) - j] == 0) {
                                            sum += (i - (int) space.get(0)) * 10;
                                        } else if (arr[i][(int) space.get(1) - j] == 1) {
                                            sum += (9 - (i - (int) space.get(0))) * 1000;
                                        }
                                    }
                                }
                            }
                            if (finSum > sum) {
                                finSum = sum;
                                fx = 1;
                                fy = 0;
                            }
                        }
                        sum = 0;

                        if (arr[(int) space.get(0) + 1][(int) space.get(1) - 1] != 1) {
                            for (int j = (int) space.get(1) - 1; j >= 0; j--) {
                                for (int i = 0; i < (int) space.get(1) - j; i++) {
                                    if (inInterval(j) && inInterval((int) space.get(0) + 1 + i)) {
                                        if (arr[(int) space.get(0) + 1 + i][j] == 0) {
                                            sum += ((int) space.get(1) - j) * 10;
                                        } else if (arr[(int) space.get(0) + 1 + i][j] == 1) {
                                            sum += (9 - ((int) space.get(1) - j)) * 1000;
                                        }
                                    }
                                }
                            }
                            if (finSum > sum) {
                                finSum = sum;
                                fx = 1;
                                fy = -1;
                            }
                        }
                        sum = 0;

                        if (arr[(int) space.get(0)][(int) space.get(1) - 1] != 1) {
                            for (int j = (int) space.get(1) - 1; j >= 0; j--) {
                                for (int i = 0; i < (int) space.get(1) - j; i++) {
                                    if (inInterval(j) && inInterval((int) space.get(0) - i)) {
                                        if (arr[(int) space.get(0) - i][j] == 0) {
                                            sum += ((int) space.get(1) - j) * 10;
                                        } else if (arr[(int) space.get(0) - i][j] == 1) {
                                            sum += (9 - ((int) space.get(1) - j)) * 1000;
                                        }
                                    }
                                }
                            }

                            if (finSum > sum) {
                                finSum = sum;
                                fx = 0;
                                fy = -1;
                            }
                        }
                        if (fx != -2 && fy != -2) {

                            arr[kitten[0]][kitten[1]] = 0;
                            if (inInterval(kitten[0] + fx) && inInterval(kitten[1] + fy)) {
                                arr[kitten[0] + fx][kitten[1] + fy] = 9;

                                kitten[0] = kitten[0] + fx;
                                kitten[1] = kitten[1] + fy;

                                space.set(0, kitten[0]);
                                space.set(1, kitten[1]);
                            } else {
                                res = false;
                            }
                        } else {
                            res = false;
                        }
                    } else {
                        res = false;
                    }
                    result = "({\"newStep\":\"" + res + "\"})";
                }
                break;
                case "getStatus": {
                    int res = 0;
                    kitten[0] = (int) space.get(0);
                    kitten[1] = (int) space.get(1);
                    if (kitten[0] == 0 || kitten[0] == spaceCount - 1
                            || kitten[1] == 0 || kitten[1] == spaceCount - 1) {
                        res = 1;    //проигрыш
                    } else {
                        boolean stop = false;
                        for (int i = -1; i < 3 && !stop; i++) {
                            for (int j = -1; j < 3 && !stop; j++) {
                                if (inInterval(kitten[0] + i) && inInterval(kitten[1] + j)) {
                                    if (arr[kitten[0] + i][kitten[1] + j] == 0) {
                                        stop = true;
                                    }
                                }
                            }
                        }

                        if (!stop) {
                            res = 2;    //выйгрышь
                        }
                    }
                    result = "({\"getStatus\":\"" + true + "\"})";
                }
                break;
                case "addAndNewStep": {
                    boolean res = true;
                    String strX = request.getParameter("x");
                    String strY = request.getParameter("y");
                    if (strX != null && strY != null) {
                        int x = Integer.parseInt(strX);
                        int y = Integer.parseInt(strY);
                        if (space != null) {
                            space.add(x);
                            space.add(y);
                            arr[x][y] = 1;

                            int sum = 0, finSum = Integer.MAX_VALUE, fx = -2, fy = -2;

                            if (arr[(int) space.get(0) - 1][(int) space.get(1) - 1] != 1) {
                                for (int i = (int) space.get(0) - 1; i >= 0; i--) {
                                    for (int j = 0; j < (int) space.get(0) - i; j++) {
                                        if (inInterval(i) && inInterval((int) space.get(1) - 1 - j)) {
                                            if (arr[i][(int) space.get(1) - 1 - j] == 0) {
                                                sum += ((int) space.get(0) - i) * 10;
                                            } else if (arr[i][(int) space.get(1) - 1 - j] == 1) {
                                                sum += (9 - ((int) space.get(0) - i)) * 1000;
                                            }
                                        }
                                    }
                                }
                                if (finSum > sum) {
                                    fx = -1;
                                    fy = -1;
                                    finSum = sum;
                                }
                            }
                            sum = 0;

                            if (arr[(int) space.get(0) - 1][(int) space.get(1)] != 1) {
                                for (int i = (int) space.get(0) - 1; i >= 0; i--) {
                                    for (int j = 0; j < (int) space.get(0) - i; j++) {
                                        if (inInterval(i) && inInterval((int) space.get(1) + j)) {
                                            if (arr[i][(int) space.get(1) + j] == 0) {
                                                sum += ((int) space.get(0) - i) * 10;
                                            } else if (arr[i][(int) space.get(1) + j] == 1) {
                                                sum += (9 - ((int) space.get(0) - i)) * 1000;
                                            }
                                        }
                                    }
                                }
                                if (finSum > sum) {
                                    finSum = sum;
                                    fx = -1;
                                    fy = 0;
                                }
                            }
                            sum = 0;

                            if (arr[(int) space.get(0) - 1][(int) space.get(1) + 1] != 1) {
                                for (int j = (int) space.get(1) + 1; j < 9; j++) {
                                    for (int i = 0; i < j - (int) space.get(1); i++) {
                                        if (inInterval(j) && inInterval((int) space.get(0) - 1 - i)) {
                                            if (arr[(int) space.get(0) - 1 - i][j] == 0) {
                                                sum += (j - (int) space.get(1)) * 10;
                                            } else if (arr[(int) space.get(0) - 1 - i][j] == 1) {
                                                sum += (9 - (j - (int) space.get(1))) * 1000;
                                            }
                                        }
                                    }
                                }
                                if (finSum > sum) {
                                    finSum = sum;
                                    fx = -1;
                                    fy = 1;
                                }
                            }
                            sum = 0;

                            if (arr[(int) space.get(0)][(int) space.get(1) + 1] != 1) {
                                for (int j = (int) space.get(1) + 1; j < (int) 9; j++) {
                                    for (int i = 0; i < j - (int) space.get(1); i++) {
                                        if (inInterval(j) && inInterval((int) space.get(0) + i)) {
                                            if (arr[(int) space.get(0) + i][j] == 0) {
                                                sum += (j - (int) space.get(1)) * 10;
                                            } else if (arr[(int) space.get(0) + i][j] == 1) {
                                                sum += (9 - (j - (int) space.get(1))) * 1000;
                                            }
                                        }
                                    }
                                }
                                if (finSum > sum) {
                                    finSum = sum;
                                    fx = 0;
                                    fy = 1;
                                }
                            }
                            sum = 0;

                            if (arr[(int) space.get(0) + 1][(int) space.get(1) + 1] != 1) {
                                for (int i = (int) space.get(0) + 1; i < 9; i++) {
                                    for (int j = 0; j < i - (int) space.get(0); j++) {
                                        if (inInterval(i) && inInterval((int) space.get(1) + 1 + j)) {
                                            if (arr[i][(int) space.get(1) + 1 + j] == 0) {
                                                sum += (i - (int) space.get(0)) * 10;
                                            } else if (arr[i][(int) space.get(1) + 1 + j] == 1) {
                                                sum += ((int) 9 - (i - (int) space.get(0))) * 1000;
                                            }
                                        }
                                    }
                                }
                                if (finSum > sum) {
                                    finSum = sum;
                                    fx = 1;
                                    fy = 1;
                                }
                            }
                            sum = 0;

                            if (arr[(int) space.get(0) + 1][(int) space.get(1)] != 1) {
                                for (int i = (int) space.get(0) + 1; i < 9; i++) {
                                    for (int j = 0; j < i - (int) space.get(0); j++) {
                                        if (inInterval(i) && inInterval((int) space.get(1) - j)) {
                                            if (arr[i][(int) space.get(1) - j] == 0) {
                                                sum += (i - (int) space.get(0)) * 10;
                                            } else if (arr[i][(int) space.get(1) - j] == 1) {
                                                sum += (9 - (i - (int) space.get(0))) * 1000;
                                            }
                                        }
                                    }
                                }
                                if (finSum > sum) {
                                    finSum = sum;
                                    fx = 1;
                                    fy = 0;
                                }
                            }
                            sum = 0;

                            if (arr[(int) space.get(0) + 1][(int) space.get(1) - 1] != 1) {
                                for (int j = (int) space.get(1) - 1; j >= 0; j--) {
                                    for (int i = 0; i < (int) space.get(1) - j; i++) {
                                        if (inInterval(j) && inInterval((int) space.get(0) + 1 + i)) {
                                            if (arr[(int) space.get(0) + 1 + i][j] == 0) {
                                                sum += ((int) space.get(1) - j) * 10;
                                            } else if (arr[(int) space.get(0) + 1 + i][j] == 1) {
                                                sum += (9 - ((int) space.get(1) - j)) * 1000;
                                            }
                                        }
                                    }
                                }
                                if (finSum > sum) {
                                    finSum = sum;
                                    fx = 1;
                                    fy = -1;
                                }
                            }
                            sum = 0;

                            if (arr[(int) space.get(0)][(int) space.get(1) - 1] != 1) {
                                for (int j = (int) space.get(1) - 1; j >= 0; j--) {
                                    for (int i = 0; i < (int) space.get(1) - j; i++) {
                                        if (inInterval(j) && inInterval((int) space.get(0) - i)) {
                                            if (arr[(int) space.get(0) - i][j] == 0) {
                                                sum += ((int) space.get(1) - j) * 10;
                                            } else if (arr[(int) space.get(0) - i][j] == 1) {
                                                sum += (9 - ((int) space.get(1) - j)) * 1000;
                                            }
                                        }
                                    }
                                }

                                if (finSum > sum) {
                                    finSum = sum;
                                    fx = 0;
                                    fy = -1;
                                }
                            }
                            if (fx != -2 && fy != -2) {

                                arr[kitten[0]][kitten[1]] = 0;
                                if (inInterval(kitten[0] + fx) && inInterval(kitten[1] + fy)) {
                                    arr[kitten[0] + fx][kitten[1] + fy] = 9;

                                    kitten[0] = kitten[0] + fx;
                                    kitten[1] = kitten[1] + fy;

                                    space.set(0, kitten[0]);
                                    space.set(1, kitten[1]);
                                } else {
                                    res = false;
                                }
                            } else {
                                res = false;
                            }
                        } else {
                            res = false;
                        }
                    } else {
                        res = false;
                    }

                    if (res) {
                        if (space == null) {
                            space = new ArrayList();
                            for (int i = 0; i < spaceCount; i++) {
                                for (int j = 0; j < spaceCount; j++) {
                                    arr[i][j] = 0;
                                }
                            }

                            final Random random = new Random();
                            space.add(spaceCount / 2);  //координаты кошки x
                            space.add(spaceCount / 2);  //координаты кошки y
                            space.add(Math.abs(random.nextInt()) % 2 + 1);  //координаты препятсвий x
                            space.add(Math.abs(random.nextInt()) % 5 + 1);  //координаты препятсвий y ...
                            space.add(Math.abs(random.nextInt()) % 5 + 1);
                            space.add(Math.abs(random.nextInt()) % 2 + 6);
                            space.add(Math.abs(random.nextInt()) % 2 + 6);
                            space.add(Math.abs(random.nextInt()) % 5 + 3);
                            space.add(Math.abs(random.nextInt()) % 5 + 3);
                            space.add(Math.abs(random.nextInt()) % 2 + 1);

                            arr[(int) space.get(0)][(int) space.get(1)] = 9;

                            arr[(int) space.get(2)][(int) space.get(3)] = 1;
                            arr[(int) space.get(4)][(int) space.get(5)] = 1;
                            arr[(int) space.get(6)][(int) space.get(7)] = 1;
                            arr[(int) space.get(8)][(int) space.get(9)] = 1;

                            kitten[0] = (int) space.get(0);
                            kitten[1] = (int) space.get(1);
                        }

                        result = "({";
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                if (arr[i][j] != 0) {
                                    result += "\"td_" + (i * 9 + j) + "\":\"" + arr[i][j] + "\"";
                                    if (i != 8 || j != 8) {
                                        result += ",";
                                    }
                                }
                            }
                        }

                        result += "\"getStatus\":\"" + getStatus() + "\"})";
                    }
                }
            }

            PrintWriter pw = response.getWriter();
            pw.println(result);
        }
    }

    public static boolean inInterval(int x) {
        boolean res = false;
        if (x >= 0 && x < spaceCount) {
            res = true;
        }
        return res;
    }

    public static int getStatus() {
        int res = 0;
        kitten[0] = (int) space.get(0);
        kitten[1] = (int) space.get(1);
        if (kitten[0] == 0 || kitten[0] == spaceCount - 1
                || kitten[1] == 0 || kitten[1] == spaceCount - 1) {
            res = 1;    //проигрыш
        } else {
            boolean stop = false;
            for (int i = -1; i < 3 && !stop; i++) {
                for (int j = -1; j < 3 && !stop; j++) {
                    if (inInterval(kitten[0] + i) && inInterval(kitten[1] + j)) {
                        if (arr[kitten[0] + i][kitten[1] + j] == 0) {
                            stop = true;
                        }
                    }
                }
            }

            if (!stop) {
                res = 2;    //выйгрышь
            }
        }
        return res;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
