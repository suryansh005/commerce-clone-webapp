package com.commerce.webapp.commerceclonewebapp.controller;


import com.commerce.webapp.commerceclonewebapp.model.CatH;
import com.commerce.webapp.commerceclonewebapp.model.entity.Category;
import com.commerce.webapp.commerceclonewebapp.model.entity.Customer;
import com.commerce.webapp.commerceclonewebapp.repository.CategoryRepository;

import com.commerce.webapp.commerceclonewebapp.repository.ProductRepository;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.CustomerService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.JwtService;
import com.commerce.webapp.commerceclonewebapp.service.interfaces.RefreshTokService;
import com.commerce.webapp.commerceclonewebapp.util.CookieUtil;
import com.commerce.webapp.commerceclonewebapp.util.JsonUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.jms.Topic;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


import static com.commerce.webapp.commerceclonewebapp.util.Constants.REFRESH_TOKEN;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokService refreshTokService;

    @Autowired
    CategoryRepository cr;
    @RequestMapping(value = "/register" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestBody String customerJson){

        Customer uiCustomer = Customer.fromJsonToCustomer(customerJson);

        String email = uiCustomer.getEmail();

        if(email==null || email.trim().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid email");
        }else {
            try {
                Customer dbCustomer = customerService.findByEmail(email);
                    if(dbCustomer!=null)
                        return ResponseEntity.ok("user already exists");
            }catch (UsernameNotFoundException e){
                /* left empty becuase customerService.findByEmail(email)
                    will throw UsernameNotFoundException when user not found in
                    Db
                * */

            }

            Customer customer = Customer.builder()
                    .email(uiCustomer.getEmail())
                    .firstName(uiCustomer.getFirstName())
                    .lastName(uiCustomer.getLastName())
                    .password(passwordEncoder.encode(uiCustomer.getPassword()))
                    .isAccountExpired(false)
                    .isCredentialsExpired(false)
                    .isActive(true)
                    .isAccountLocked(false)
                    .contactNumber(uiCustomer.getContactNumber())
                    .countryCode(uiCustomer.getCountryCode())
                    .build();
            customerService.register(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body("user created");
        }
    }
    @RequestMapping(value = "/test")
    @ResponseBody
    public ResponseEntity<?> test(){
        FileInputStream fis =null;

        try {
            fis = new FileInputStream(new File("C:\\personal\\catego.xlsx"));
//            File file = new File("C:\\personal\\catego.xlsx");
            Set<String>allXcellCats =  new HashSet<>();
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for(int i =0 ;i<=sheet.getLastRowNum();i++){
                XSSFRow row = sheet.getRow(i);
                for(int j=0;j<row.getLastCellNum();j++){
                    if(row.getCell(j)!=null && !row.getCell(j).toString().trim().isEmpty()){
                        Arrays.asList(row.getCell(j).toString().split("\\|")).stream().map(item -> item.toUpperCase()).forEach(allXcellCats::add);
                    }
                }
            }

            Map<String, CatH>hierarchy =  new HashMap();

            for(String ttt:allXcellCats){
                String [] testArr = ttt.split(">");
                int idx=0;
                while(idx<testArr.length){

                    CatH obj = findNode(hierarchy,testArr,0,idx);

                    if(obj != null){
                        idx++;
                        continue;
                    }
                    Category currentCat = new Category(testArr[idx].toUpperCase());
                    CatH parentNode = findNode(hierarchy,testArr,0,idx-1);

                    Category parentCat =  parentNode == null ? null : parentNode.getCategory();
                    currentCat.setParent(parentCat);
                    if(parentCat!=null)
                        parentCat.addChildren(currentCat);

                    CatH currentNode = new CatH(currentCat.getCategoryName().toUpperCase());
                    currentNode.setCategory(currentCat);

                    if (parentNode!=null){
                        parentNode.getHierarchy().put(testArr[idx].toUpperCase(),currentNode);
                    }else
                        hierarchy.put(currentNode.getName(), currentNode);

                    idx++;
                }
            }
            for (Map.Entry<String,CatH> cath : hierarchy.entrySet()){
                cr.save(cath.getValue().getCategory());
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fis!=null) {
                try {
                    fis.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return ResponseEntity.ok().body("heelo");
    }

    public CatH findNode(Map<String, CatH>hierarchy,String []testArr,int current ,int required){
       if(required< 0)
           return null;

        CatH obj = hierarchy.get(testArr[current]);
        if(obj ==  null)
            return null;

        else if (obj.getCategory().getCategoryName().equalsIgnoreCase(testArr[required]))
            return obj;

        return obj.findNode(testArr,++current,required);
    }

    @RequestMapping(value = "/refresh-token")
    @ResponseBody
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response){
        Cookie refreshCookie = CookieUtil.getCookieByName(request,REFRESH_TOKEN);

        if(refreshCookie != null){
            String refreshTokenStr =  refreshCookie.getValue();
            try {
            ////All verification already done in RefreshToknFilter
                String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                Customer user =  customerService.findByEmail(userEmail);
                String newJwtToken =  jwtService.generateToken(user);
                response.addCookie(CookieUtil.generateJwtCookie(newJwtToken));

                return JsonUtil.genericSuccess();

            }catch (Exception e){
                CookieUtil.deleteCookie(response,request,REFRESH_TOKEN);
                System.out.println(e.getMessage());
                return JsonUtil.genericUnauthorized();
            }

        }
        System.out.println("refreshToken cookie absent");
        return JsonUtil.genericUnauthorized();
    }

}
