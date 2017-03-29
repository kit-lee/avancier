package com.muses;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import com.muses.avancier.AvancierApplication;
import com.muses.avancier.model.Activity;
import com.muses.avancier.model.WxUser;
import com.muses.avancier.service.ActivityService;
import com.muses.avancier.service.WxUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=AvancierApplication.class)
public class AvancierApplicationTests {
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private WxUserService wxUserService;

	@Test
	public void contextLoads() {
	    Pageable page = new PageRequest(0, 10, new Sort(Direction.DESC, "id"));
	    Page<Activity> pages = activityService.listActivities(page);
	    Activity defAct = pages.getContent().get(0);
	    WxUser user = new WxUser();
	    user.setActivity(defAct);
	    user.setChecked(false);
	    user.setCreatetime(new Date());
	    user.setHeadpic("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1092833709,4270747472&fm=111&gp=0.jpg");
	    user.setMessage("我都来啦，你地今日有乜搞啊");
	    user.setNickname("老王");
	    user.setTrans(false);
	    user.setOpenId("111111122222222222333336");
	    wxUserService.saveUser(user);
	}

}
