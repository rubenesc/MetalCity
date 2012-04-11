/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.jsf.reports;

import co.com.metallium.core.constants.Navegation;
import co.com.metallium.core.entity.stats.MemoryStat;
import co.com.metallium.core.service.GeneralService;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.core.util.StaticApplicationParameters;
import co.com.metallium.view.jsf.BaseManagedBean;
import co.com.metallium.view.jsf.components.PaginationComp;
import com.metallium.utils.dto.RequestInfoDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Ruben
 */
@ManagedBean(name = "report")
@ViewScoped
public class ReportManagedBean extends BaseManagedBean implements Serializable {

    @ManagedProperty(value = "#{paginationComp}")
    private PaginationComp paginationComp;
    @EJB
    GeneralService generalService;
    List<MemoryStat> memoryStatList = null;
    List<RequestInfoDTO> clientIpList = null;
    String clientIpListString = null;
    Integer clientIpSize = null;


    /** Creates a new instance of ReportManagedBean */
    public ReportManagedBean() {
    }

    public String loadMemoryReport() {
        String answer = Navegation.PRETTY_HOME; //I assume the data is not going to be loaded correctly thus I'll send the requqest back to the home location.
        try {

            if (true) {
                memoryStatList = new ArrayList<MemoryStat>();
                memoryStatList.add(MetUtilHelper.getMemoryStats());

                clientIpList =  StaticApplicationParameters.getClientIpList();
                
                clientIpListString = buildClientListString(clientIpList);

                clientIpSize = clientIpList.size();


            } else {
                memoryStatList = generalService.getMemoryStatForReport();

            }
            answer = Navegation.PRETTY_OK; //The data was loaded correctly
        } catch (Exception ex) {
            manageException(ex);
        }

        return answer;


    }

    public List<MemoryStat> getMemoryStatList() {
        return memoryStatList;
    }

    public List<RequestInfoDTO> getClientIpList() {
        return clientIpList;
    }

    public String getClientIpListString() {
        return clientIpListString;
    }

    public Integer getClientIpSize() {
        return clientIpSize;
    }

    public PaginationComp getPaginationComp() {
        return paginationComp;
    }

    public void setPaginationComp(PaginationComp paginationComp) {
        this.paginationComp = paginationComp;
    }

    private String buildClientListString(List<RequestInfoDTO> clientIpList) {
        StringBuilder builder = new StringBuilder();
        for (Iterator<RequestInfoDTO> it = clientIpList.iterator(); it.hasNext();) {
            RequestInfoDTO requestInfoDTO = it.next();
            builder.append(requestInfoDTO.getIp());
            builder.append(", ");
        }
        return builder.toString();
    }
}
