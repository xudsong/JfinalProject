package hisense.code.controller.login;

import hisense.code.controller.ApiController;
import hisense.code.interceptor.BusinessException;
import hisense.code.interceptor.annotation.AuthCode;
import hisense.code.model.TUser;
import hisense.code.model.dto.BusinessUserDto;
import hisense.code.model.dto.ServiceResult;
import hisense.code.utils.SessionUtil;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.upload.UploadFile;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class TestController extends ApiController {
    Logger logger = Logger.getLogger(TestController.class);


    public void test() {
        render("index.html");
    }

    public void upload() {
        render("upload.html");
    }

    public void doupload() {
        HttpServletRequest request = getRequest();
        String basePath = request.getContextPath();
        //存储路径
//        String path = getSession().getServletContext().getRealPath("upload/test/aaaa/");
        String path = "aaaa\\";
        UploadFile file = getFile("file", path);
        System.out.println(path);
        String fileName = "";
        if (file.getFile().length() > 200 * 1024 * 1024) {
            System.err.println("文件长度超过限制，必须小于200M");
        } else {
            //上传文件
            String type = file.getFileName().substring(file.getFileName().lastIndexOf(".")); // 获取文件的后缀
            fileName = System.currentTimeMillis() + type; // 对文件重命名取得的文件名+后缀

            String dest = path + "/" + fileName;
            file.getFile().renameTo(new File(dest));
        }

        renderJson("1111111");
    }

    @Override
    protected void get() {
        String id = getPara("id");
        renderJson(id);
    }

    @Override
    protected void post() {
        ServiceResult<TUser> result = new ServiceResult<>();
        try {
            String data = HttpKit.readData(getRequest());
            TUser tUser = FastJson.getJson().parse(data, TUser.class);
            result.setResult(tUser);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("[TestController.post]:", e);
            result.setError(e);
        }
        renderJson(result);
    }

    public void setCode() {
//        int id = getParaToInt("id");
        BusinessUserDto businessUserDto = new BusinessUserDto();
        String[] code = {"aaaaaaa"};
        businessUserDto.setAuthCode(code);
//        TUser tUser = new TUser();
//        tUser.setId(id);
        String token = SessionUtil.setUserInfo(businessUserDto);
        renderJson(token);
    }

    @AuthCode(code = {"aaaaaaab"})
    public void qaaa() {
        renderJson("fffffffff");
    }

    public void error() {
        throw new BusinessException("出错了");
    }
}
