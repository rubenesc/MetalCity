/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.view.util;

import co.com.metallium.core.entity.Network;
import co.com.metallium.core.entity.User;
import co.com.metallium.core.util.ApplicationParameters;
import co.com.metallium.core.util.MetUtilHelper;
import co.com.metallium.view.jsf.filter.SecurityFilter;
import com.metallium.utils.framework.jsf.FacesUtils;
import com.metallium.utils.framework.utilities.LogHelper;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.validator.ValidatorException;

/**
 * 20101105
 * @author Ruben
 */
public abstract class GeneralConverter {

    //WTFIS ??? = What the fuck is this ???
    //Well ... try injecting an EJB in a converter and tell me how it goes.
    //This returns a reference with a Session EJB.
    public abstract ApplicationParameters getApplicationParameters();

    public GeneralConverter() {
    }

    /**
     * 
     * @return
     */
    public Converter getProfileConverter() {
        return new Converter() {

            public Object getAsObject(FacesContext context, UIComponent component, String newValue) throws ConverterException {
                return null;
            }

            public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
                String answer = "";
                Integer profileId = null;
                try {

                    if (value != null) {

                        if (value instanceof String) {
                            profileId = Integer.parseInt((String) value);
                        } else if (value instanceof Integer) {
                            profileId = (Integer) value;
                        }


                        answer = getApplicationParameters().getProfile(profileId);
                    }

                } catch (Exception ex) {
                    String msg = "Error in JSF Converter, profile with id: " + profileId + ", could not be converted. Error: " + ex.getMessage();
                    LogHelper.makeLog(msg, ex);
                }
                return answer;
            }
        };
    }

    public Converter getSexConverter() {

        return new Converter() {

            public Object getAsObject(FacesContext context, UIComponent component, String newValue) throws ConverterException {
                return null;
            }

            public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
                String answer = "";
                String id = null;
                try {

                    if (value != null) {
                        if (value instanceof String) {
                            id = (String) value;
                            answer = getApplicationParameters().getSex(id);
                        }
                    }
                } catch (Exception ex) {
                    String msg = "Error in JSF Converter, sex with id: " + id + ", could not be converted. Error: " + ex.getMessage();
                    LogHelper.makeLog(msg, ex);
                }
                return answer;
            }
        };
    }

    public Converter getGalleryVisibilityConverter() {
        return new Converter() {

            public Object getAsObject(FacesContext context, UIComponent component, String newValue) throws ConverterException {
                return null;
            }

            public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
                String answer = "";
                String id = null;
                try {

                    if (value != null) {
                        if (value instanceof String) {
                            id = (String) value;
                            answer = getApplicationParameters().getVisibility(id);
                        }
                    }

                } catch (Exception ex) {
                    String msg = "Error in JSF Converter, visibility with id: " + id + ", could not be converted. Error: " + ex.getMessage();
                    LogHelper.makeLog(msg, ex);
                }
                return answer;
            }
        };
    }

    public Converter getNewsStateConverter() {
        return new Converter() {

            public Object getAsObject(FacesContext context, UIComponent component, String newValue) throws ConverterException {
                return null;
            }

            public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
                String answer = "";
                String id = null;
                try {

                    if (value != null) {
                        if (value instanceof String) {
                            id = (String) value;
                            answer = getApplicationParameters().getNewsState(id);
                        }
                    }

                } catch (Exception ex) {
                    String msg = "Error in JSF Converter, news state with id: " + id + ", could not be converted. Error: " + ex.getMessage();
                    LogHelper.makeLog(msg, ex);
                }
                return answer;
            }
        };
    }

    public Converter getUserStateConverter() {
        return new Converter() {

            public Object getAsObject(FacesContext context, UIComponent component, String newValue) throws ConverterException {
                return null;
            }

            public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
                String answer = "";
                String id = null;
                try {

                    if (value != null) {
                        if (value instanceof String) {
                            id = (String) value;
                            answer = getApplicationParameters().getUserState(id);
                        }
                    }

                } catch (Exception ex) {
                    String msg = "Error in JSF Converter, news state with id: " + id + ", could not be converted. Error: " + ex.getMessage();
                    LogHelper.makeLog(msg, ex);
                }
                return answer;
            }
        };
    }

    public Converter getNetworkConverter() {
        return new Converter() {

            public Object getAsObject(FacesContext facesContext, UIComponent component, String newValue) throws ConverterException {
                if (newValue.trim().equals("")) {
                    return null;
                } else {
                    try {
                        int id = Integer.parseInt(newValue);           //this case is just for the primefaces picklist component
                        return getApplicationParameters().getNetwork(id);
                    } catch (Exception ex) {
                        String msg = "Error in JSF Converter, network AsObject with id: " + newValue + ", could not be converted. Error: " + ex.getMessage();
                        LogHelper.makeLog(msg, ex);
                    }
                }

                return null;
            }

            public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
                String answer = "";
                String id = null;
                try {

                    if (value != null) {
                        if (value instanceof String) {
                            id = (String) value;
                            answer = getApplicationParameters().getNetwork(Integer.valueOf(id)).getName();
                        } else if (value instanceof Integer) {
                            answer = getApplicationParameters().getNetwork((Integer) value).getName();
                        } else if (value instanceof Network) {
                            answer = ((Network) value).getId().toString(); //This case is for the Primefaces Picklist component
                        } else if (value instanceof Collection) {
                            /**
                             * Aux method that builds in a String the list of all the networks that are
                             * associated with this news.
                             */
                            List<Network> networkCollection = (List<Network>) value;
                            if (networkCollection != null && !networkCollection.isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                for (Iterator<Network> it = networkCollection.iterator(); it.hasNext();) {
                                    sb.append(it.next().getName());
                                    if (it.hasNext()) {
                                        sb.append(", ");
                                    }
                                }
                                answer = sb.toString();
                            } else {
                                answer = getApplicationParameters().getResourceBundleMessage("common_info_none");
                            }
                        }
                    }

                } catch (Exception ex) {
                    String msg = "Error in JSF Converter, network with id: " + id + ", could not be converted. Error: " + ex.getMessage();
                    LogHelper.makeLog(msg, ex);
                }
                return answer;
            }
        };
    }

    public Converter getSendMessageToUser() {
        return new Converter() {

            public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) throws ConverterException {
                if (submittedValue.trim().equals("")) {
                    return null;
                } else {

                    User authenticatedUser = null;
                    try {
                        authenticatedUser = (User) FacesUtils.getManagedBean(SecurityFilter.AUTHENTICATED_USER);
                    } catch (NullPointerException e) {
                        //This is normal to happen when you are not logged in
                    } catch (Exception e) {
                        LogHelper.makeLog(e.getMessage(), e);
                    }

                    return authenticatedUser;

//                        int number = Integer.parseInt(submittedValue);
//
//                        for(Player p : playerDB) {
//                                if(p.getNumber() == number)
//                                        return p;
//                        }
                }

                //  return null;
            }

            public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
                String answer = "";
                String id = null;
                try {

                    if (value != null) {
                        if (value instanceof String) {
                            id = (String) value;
                            answer = getApplicationParameters().getSex(id);
                        }
                    }

                } catch (Exception ex) {
                    String msg = "Error in JSF Converter, sex with id: " + id + ", could not be converted. Error: " + ex.getMessage();
                    LogHelper.makeLog(msg, ex);
                }
                return answer;
            }
        };
    }

    public void validateId(FacesContext context, UIComponent component, Object value) {

        if (MetUtilHelper.isValidNumber(value.toString())) {
            return;
        }

        throw new ValidatorException(new FacesMessage("invalid input"));
    }

    public Converter getUrlDomainConverter() {
        return new Converter() {

            public Object getAsObject(FacesContext context, UIComponent component, String newValue) throws ConverterException {
                return null;
            }

            public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
                String answer = "";
                String id = null;
                try {

                    if (value != null) {
                        if (value instanceof String) {
                            answer = MetUtilHelper.getDomainUrl((String) value);
                            if (answer == null) {
                                answer = (String) value; //Well if I couldnt get the domain then I'll return the value itself.
                            }
                        }
                    }

                } catch (Exception ex) {
                    String msg = "Error in JSF Converter, news state with id: " + id + ", could not be converted. Error: " + ex.getMessage();
                    LogHelper.makeLog(msg, ex);
                }
                return answer;
            }
        };
    }
}
