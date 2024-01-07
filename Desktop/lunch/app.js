var cron = require('node-cron');
const axios = require('axios');
//app.js
const Mattermost = require('node-mattermost');
const hookurl = "https://meeting.ssafy.com/hooks/iqerwsky1frp9eym11tq6q4i8e"; // 상단에서 구한 hook url
const mattermost = new Mattermost(hookurl);

function formatNum(num) {
    // 숫자를 두 자리 문자열로 변환
    const formattedDay = num < 10 ? `0${num}` : `${num}`;
    return formattedDay;
  }

// 프사 설정을 위해
function base64_encode(file) {
    var bitmap = fs.readFileSync(file);
    return bitmap.toString('base64');
}

cron.schedule('8 14 * * *', function(){
    let today = new Date();
    var month = formatNum(today.getMonth()+1);
    var date = formatNum(today.getDate()); 
    let hour = today.getHours();
    let minutes = today.getMinutes();
    console.log(`날자: ${month}${date}`);
    if(true){
        const apiUrl = `https://api.serotina.site/get_menu/${month}${date}`
    
        axios.get(apiUrl).then(response => {
            const american = response.data.american;
            const korea = response.data.korean;
    
            // 변수 값 출력 또는 다른 작업 수행
            console.log(`korea: ${korea}`);
            console.log(`american: ${american}`);
 
            mattermost.send({
                text: `한식 = ${korea}\n양식 = ${american}`,
              
                // 채널명을 입력해준다. 채널명이 한글이면 좀 다른데 아래에서 설명..
                channel: 'testing',
                username: '점심식사 봇',
                // 프로필 사진을 지정해준다. 나는 같은 폴더에 있는 케이크 사진을 base64 형태로 추가했다 
                //icon_url: 'data:image/png;base64,' + base64_encode('./public/img/cake.png'),
              
              })
        })
      .catch(error => {
        console.error('Error fetching data:', error);
      });
    }
});
