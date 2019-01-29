package e.darom.mywebbrowser

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

//가상머신을 통해 실행해본 결과 사이트 url을 입력한 후 홈페이지메뉴로 이동하면 검색했던 사이트 url이 위에 계속 떠있는다.
//현재보고있는 홈페이지 url로 항상 갱신하고 싶다.
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //웹 뷰 기본 설정 : 웹뷰 기본 설정과 구글 페이지를 로딩하는 코드
        /*
        웹뷰를 사용할 때 항상 기본으로 두 가지 설정을 해야 합니다.
        첫째, javaScriptEnabled기능을 켭니다. 그래야 자바스크립트 기능이 잘 동작합니다.
        둘째, webViewClient는 WebViewClient 클래스를 지정하는데 이것을 지정하지 않으면 웹뷰에 페이지가 표시되지 않고 자체 웹 브라우저가 동작합니다.
        웹뷰를 사용할 때는 이 두 옵션의 설정을 잊지 마세요.
        */
        webView.apply{
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
        //loadUrl() 메서드를 사용하여 "http://"가 포함된 Url을 전달하면 웹뷰에 페이지가 로딩됩니다.
        webView.loadUrl("http://www.google.com")


        // 소프트 키보드의 검색 버튼 동작 정의하기
        /*
        1. EditText의 setOnEditorActionListener는 에디트텍스트가 선택되고 글자가 입력될 때마다 호출됩니다.
        인자로는 반응한 뷰, 액션ID, 이벤트 세 가지며, 여기서는 뷰와 이벤트를 사용하지 않기 때문에 _로 대치할 수 있습니다.

        2. actionId 값은 EditorInfo 클래스에 상수로 정의된 값 중에서 검색 버튼에 해당하는 상수와 비교하여 검색 버튼이 눌렸는지 확인합니다.

        3. 검색 창에 입력한 주소를 웹뷰에 전달하여 로딩합니다. 마지막으로 true를 반환하여 이벤트를 종료합니다.

        앱을 실행하여 검색 창에 http://를 포함하여 입력한 URL대로 이동하면 성공입니다.
         */
        urlEditText.setOnEditorActionListener{_, actionId, _ -> //1.
            if(actionId == EditorInfo.IME_ACTION_SEARCH){       //2.
                webView.loadUrl(urlEditText.text.toString())    //3.
                true
            } else{
                false
            }
        }

        //컨텍스트 메뉴 등록 : 메서드에 컨텍스트 메뉴를 표시할 뷰에 웹뷰를 지정했습니다.
        registerForContextMenu(webView)
    }
    //뒤로가기 동작 재정의
    /*
    1. 웹뷰가 이전 페이지로 갈 수 있다면
    2. 이전 페이지로 이동하고

    3. 그렇지 않다면 원래동작을 수행합니다. 즉 액티비티를 종료합니다.

    앱을 실행하여 자유롭게 웹을 탐험하다가 뒤로가기를 눌러봅니다. 이전 페이지로 이동하고 더 이상 이전 페이지가 없을 때 앱이 종료되면 성공입니다.
     */
    override fun onBackPressed(){
        if(webView.canGoBack()){    //1.
            webView.goBack()        //2.
        } else{
            super.onBackPressed()   //3.
        }
    }

    //옵션메뉴 리소스 지정 : 옵션 메뉴를 액티비티에 표시하기
    /*
    1. 메뉴 리소스를 액티비티의 메뉴로 표시하려면 menuInflater 객체의 inflate() 메서드를 사용하여 메뉴 리소스를 지정합니다.
    2. true를 반환하면 액티비티에 메뉴가 있다고 인식합니다.

    앱을 실행하여 메뉴가 표시되면 성공입니다.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu) //1.
        return true                             //2.
    }

    //옵션 메뉴의 처리 : 옵션 메뉴 클릭 이벤트 처리
    /*
    옵션 메뉴를 선택했을 때의 이벤트를 처리하려면 onOptionsItemSelected() 메서드를 오버라이드하여 메뉴 아이템의 ID로 분기하여 처리합니다.
    분기처리를 위해 when문을 작성합니다.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){                                    //1. 메뉴 아이템으로 분기를 수행합니다.
            R.id.action_google, R.id.action_home -> {           //2. 구글, 집 아이콘을 클릭하면 구글 페이지를 로딩합니다.
                webView.loadUrl("http://www.google.com")
                return true
            }
            R.id.action_naver -> {                              //3. 네이버를 클릭하면 네이버 페이지를 로딩합니다.
                webView.loadUrl("http://www.naver.com")
                return true
            }
            R.id.action_daum -> {                               //4. 다음을 클릭하면 다음 페이지를 로딩합니다.
                webView.loadUrl("http://www.daum.net")
                return true
            }
            /*
            암시적 인텐트는 대부분 이러한 형태를 하고 있습니다.
            1- 인텐트를 정의하며 Intent 클래스에 정의된 액션 중 하나를 지정합니다. ACTION_DIAL 액션은 전화 다이얼을 입력해주는 액션입니다.

            2- 인텐트에 데이터를 저장합니다. 'tel:'로 시작하는 Uri는 전화번호를 나타내는 국제표준 방법입니다.
            이를 Uri.parse() 메서드로 감싼 Uri 객체를 데이터로 설정합니다.

            3- intent.resolveActivity() 메서드는 이 인텐트를 수행하는 액티비티가 있는지를 검사하여 반환합니다. null이 반환된다면 수행하는 액티비티가 없는 겁니다.
            전화 앱이 없는 태블릿 같은 경우에 해당됩니다.

            => Anko 라이브러리를 사용하면 훨씬 쉽게 암시적 인텐트를 활용할 수 있습니다.
            전화걸기 : makeCall(전화번호)
            문자 보내기 : senSms(전화번호, [문자열])
            웹 브라우저에서 열기 : browse(url)
            문자열 공유 : share(문자열, [제목])
            이메일 보내기 : email(받는 메일주소, [제목], [내용])

            @ 단, 전화걸기는 Anko에서 지원하는 방법을 사용하지 않았습니다. 전화걸기는 별도의 권한을 추가해야 하고 사용자는 다이얼 입력까지만 제공해도
            스스로 전화를 걸면 되기 때문입니다. 필자는 전화걸기 암시적 인텐트는 특별한 경우가 아니면 사용하지 않는 것을 권합니다. @
             */
            R.id.action_call -> {                               //5. 연락처를 클릭하면 전화 앱을 엽니다. 이러한 방식을 암시적 인텐트라고 합니다.(7.7절)
                val intent = Intent(Intent.ACTION_DIAL)                 //1-
                intent.data = Uri.parse("tel:031-123-4567")     //2-
                if (intent.resolveActivity(packageManager) != null){    //3-
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {                          //6. 문자 보내기 코드를 작성합니다.
                //문자보내기 : 여러분의 전화번호를 설정하여 문자가 발송되는지 확인하기 바랍니다.
                sendSMS("031-123-4567", webView.url)
                return true
            }
            R.id.action_email -> {                              //7. 이메일 보내기 코드를 작성합니다.
                //이메일 보내기 : 여러분의 이메일을 설정하여 메일이 발송되는지 확인하기 바랍니다.
                email("test@example.com", "좋은 사이트", webView.url)
                return true
            }
        }
        return super.onOptionsItemSelected(item)                //8. when 문에서는 각 메뉴 처리를 끝내고 true를 반환했습니다. 내가 처리하고자 하는 경우를
                                                        //제외한 그 이외의 경우에는 super 메서드를 호출하는 것이 안드로이드 시스템에서의 보편적인 규칙입니다.
    }


    //컨텍스트 메뉴 등록하기 : 코드는 옵션 메뉴와 같습니다.
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu) // menuInflater.inflate()메서드를 사용하여 메뉴 리소스를 액티비티의 컨텍스트 메뉴로 사용하도록 합니다.
    }
    //컨텍스트 메뉴 클릭 이벤트 처리 : 코드 구성은 옵션 메뉴와 똑같습니다.
    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> {
                //페이지 공유 : 웹 페이지 주소를 문자열로 공유하는 앱을 사용해 공유합니다.
                share(webView.url)
                return true
            }
            R.id.action_browser -> {
                //기본 웹 브라우저에서 열기 : 기기에 기본 브라우저로 웹 페이지 주소를 다시 엽니다.
                browse(webView.url)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
