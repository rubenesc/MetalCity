/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.metallium.core.service;

//import co.com.metallium.core.constants.MetConfiguration;
//import co.cybercore.framework.utilities.LogHelper;
//import java.util.Calendar;
//import java.util.Formatter;
//import javax.annotation.Resource;
//import javax.ejb.EJB;
//import javax.ejb.Singleton;
//import javax.ejb.Timeout;
//import javax.ejb.TimerService;

/**
 *
 * @author Ruben
 */
//@Singleton
public class MemoryTimer2 {

//    @EJB
//    GeneralService generalService;
//    @Resource
//    TimerService timerService;

    public MemoryTimer2() {
    }

//    /**
//     * This method is to Initialize the timer. I rather do it form a Web Application Listener
//     * rather than on a @Startup or @Postconstuct, because I want just one place to
//     * initialize everything (variables, timers, etc) as to having everything initialize by it self randomly.
//     */
    public void initializeTimer() {
//        long intevalo = MetConfiguration.TIMER_MEMORY_STATS_INTERVAL;
//
//        intevalo = intevalo * 60 * 1000; //Convert the interval which is is minutes to miliseconds.
//
//        // We create a time format in military hour
//        // 24hours:minutes:seconds example "16:10:02"
//        Formatter fmt = new Formatter();
//        Calendar cal = Calendar.getInstance();
//        fmt.format("%tk:%tM:%tS", cal, cal, cal);
//
//        String info = MemoryTimer2.class.getSimpleName() + " - started at: " + fmt;
//        timerService.createTimer(intevalo, intevalo, info);
//        LogHelper.makeLog(MemoryTimer2.class.getSimpleName() + " initialized: " + intevalo);
//
    }
//
//    @Timeout
//    public void initiateFlush() {
//        generalService.logMemoryStat();
//    }
}
