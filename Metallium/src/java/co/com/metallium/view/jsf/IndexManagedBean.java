/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf;

import co.com.metallium.core.service.NewsService;
import com.metallium.utils.framework.utilities.LogHelper;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ejb.EJB;

/**
 *
 * @author Ruben
 */
@ManagedBean(name = "indexManagedBean")
@RequestScoped
public class IndexManagedBean extends BaseManagedBean  implements Serializable {

    @EJB private NewsService newsService;

    public IndexManagedBean() {
    }

    @PostConstruct
    private void initialize() {
        LogHelper.makeLog("Estamos en IndexManagedBean.initialize");
    }

    
    public void a(){
    }



}
