$('.addManyFormCheck').on('change', function () {
    $('input[name="addManyFormCheck"]').val($(this).is(':checked') ? 'true' : 'false');
 });

 $(".addManyForm").delegate('.addManyFormCheck', 'click', function(){
    console.log($(this).is(':checked'), $(this).attr('id'));
    id = $(this).attr('id') + 'Many';
    if ($(this).is(':checked'))
       $(`#${id}`).prop('disabled', false);
    else
       $(`#${id}`).prop('disabled', true);
 })
 
 $('.addManyForm').delegate("#addMany", "click", function()
 {
    console.log('works');
    mark = document.forms.regManyForm.markMany.value;
    diameter = document.forms.regManyForm.diameterMany.value;
    packing = document.forms.regManyForm.packingMany.value;
    plav = document.forms.regManyForm.plavMany.value;
    part = document.forms.regManyForm.partMany.value;
    mass = document.forms.regManyForm.massMany.value;
    comment = document.forms.regManyForm.commentMany.value;
    manufacturer = document.forms.regManyForm.manufacturerMany.value;
    count = document.forms.regManyForm.number.value;
    console.log(mark, diameter, packing, plav, part, mass, comment, manufacturer);
    object = {
       "part": part,
       "mark": mark,
       "diameter": diameter,
       "plav": plav
    };
    $.ajax(
    {
       type: 'POST',
       url: `http://ferro-trade.ru/api/add/validate`,
       contentType: "application/json; charset=utf-8",
       dataType: 'json',
       data: JSON.stringify(object),
       success: function(data, textStatus)
       {
          if (data){
             document.forms.regManyForm.number.value = 1;
             $('.addManyForm').fadeOut();
             checked = [];
             elems = $("input:checkbox[name=addManyFormCheck]:checked");
             for (i = 0; i < elems.length; i++){
                checked.push(elems[i].id);
                elems[i].checked = false;
                $(`#${elems[i].id + 'Many'}`).prop('disabled', 'true');
             }
             console.log(checked);
             products = '';
             product = card_for_many_reg(checked.includes('mark'), checked.includes('diameter'), checked.includes('packing'), checked.includes('part'), 
             checked.includes('plav'), checked.includes('mass'), checked.includes('manufacturer'), checked.includes('comment'), mark, diameter, packing,
             part, plav, mass, manufacturer, comment);
             for (i = 0; i < count; i++){
                products += product;
             }
             btn = `<div class="row col-12">
             <button class="btn btn-outline-success btn-block col-9" id="regMany">Зарегистрировать</button>
             <div class="col-3">Вес: <span class="addManyMass">0</span></div>
             </div>`;
             $("#content").html(btn + products);
             $("#content").unbind('click').delegate('#regMany', 'click', function(){
                $("#regMany").prop("disabled", true);
                elems = $('.productInput');
                objs = [];
                for (i = 0; i < elems.length; i++){
                   if (checked.includes('mark'))
                      queryMark = mark;
                   else
                      queryMark = $(elems[i].lastChild).find('#mark')[0].value;
                   
                   if (checked.includes('diameter'))
                      queryDiameter = diameter;
                   else
                      queryDiameter = $(elems[i].lastChild).find('#diameter')[0].value;
                   
                   if (checked.includes('packing'))
                      queryPacking = packing;
                   else
                      queryPacking = $(elems[i].lastChild).find('#packing')[0].value;
                   
                   if (checked.includes('part'))
                      queryPart = part;
                   else
                      queryPart = $(elems[i].lastChild).find('#part')[0].value;
                   
                   if (checked.includes('plav'))
                      queryPlav = plav;
                   else
                      queryPlav = $(elems[i].lastChild).find('#plav')[0].value;
                   
                   if (checked.includes('mass'))
                      queryMass = mass;
                   else
                      queryMass = $(elems[i].lastChild).find('#mass')[0].value;
                   
                   if (checked.includes('manufacturer'))
                      queryManufacturer = manufacturer;
                   else
                      queryManufacturer = $(elems[i].lastChild).find('#manufacturer')[0].value;
                   
                   if (checked.includes('comment'))
                      queryComment = comment;
                   else
                      queryComment = $(elems[i].lastChild).find('#comment')[0].value;
                   queryMass = parseFloat(queryMass.replaceAll(',', '.'));
                   console.log(queryMark, queryDiameter, queryPacking, queryPart, queryPlav, queryMass, queryManufacturer, queryComment);
                   obj = {
                      "mark": queryMark,
                      "diameter": queryDiameter,
                      "packing": queryPacking,
                      "comment": queryComment,
                      "part": queryPart,
                      "plav": queryPlav,
                      "manufacturer": queryManufacturer,
                      "mass": queryMass
                   }
                   objs.push(obj);
                }
                console.log(objs);
                $.ajax({
                   type: 'POST',
                   url: `http://5.200.47.32:80/api/manufacture/add`,
                   contentType: "application/json; charset=utf-8",
                   data: JSON.stringify(objs),
                   dataType: 'json',
                   success: function(data, textStatus){
                      console.log(data);
                      if (data.package != null){
                        products = `<div class="row col-12"><button class="btn btn-outline-info btn-block mb-2" onclick="PrintCode(${data.id}); return false;">Печать всех позиций</button></div>
                        <div class="row col-12"><button class="btn btn-outline-info btn-block mb-2" onclick="PrintPackage(${data.package}); return false;">Печать поддона</button></div>`;
                      }
                      else{
                        products = `<div class="row col-12"><button class="btn btn-outline-info btn-block mb-2" onclick="PrintCode(${data.id}); return false;">Печать всех позиций</button></div>`;
                      }
                      data.id.forEach(function(id){
                         products += card_to_print(id);
                      })
                      $("#regMany").prop("disabled", false);
                      $("#content").html(products);
                   }
                })
             })
          }else{
             $('.partError').fadeIn();
             setTimeout(function (){
                $('.partError').fadeOut();
              }, 2000);
          }
       }
    })
 })

function bind_preTransfer(){
   $('#preTransferValues').keyup(function(event){
      elems = document.querySelectorAll('#preTransferValues');
      console.log($("#preTransferValues").is(':focus'));
      if (event.keyCode == 13 && elems.length != 0 && $("#preTransferValues").is(':focus')){
         $(this).val($(this).val() + ', ');
         console.log($(this).val());
         $.ajax({
            type: 'GET',
            url: `http://5.200.47.32/api/departure?query=${$(this).val()}`,
            success: function(data, textStatus){
               console.log(data.toFixed(2));
               $(".massInfo").html(data.toFixed(2));
            }
         })
      }
      return false;
   })

   $('#content').unbind('click').delegate('#preTransfer', 'click', function(){
      console.log($("#preTransferValues").is(":focus"));
      request = document.forms.preTransferForm.preTransferValues.value;
      console.log(request);
      obj = {
         "request": request
      }
      $.ajax({
         type: 'POST',
         url: `http://5.200.47.32:80/api/manufacture/departurePreProcess`,
         dataType: 'json',
         contentType: 'application/json; charset=utf-8',
         data: JSON.stringify(obj),
         success: function(data, textStatus)
         {
            console.log(data);
            products = table(data.table);
            form = `<div class="row transferForm col-12">
             <div class="col-md-12">
                 <form class="transferForm text-center" name="transferForm">
                     <div class="row">
                         <label for="destination">Пункт назначения</label>
                         <select class="form-control" name="destination" id="destination">
                             <option>Солнечногорск</option>
                         </select>
                     </div>
                     <div class="row">
                         <label for="carPlate">Номер машины</label>
                         <input type="text" class="form-control" name="carPlate" id="carPlate">
                     </div>
                     <div class="row">
                         <label for="account">Счет</label>
                         <input type="text" class="form-control" name="account" id="account">
                     </div>
                 </form>
                 <button class="btn btn-outline-success btn-block mt-2" id="transfer">Отгрузить</button>
             </div>
         </div><div class='cards col-12 row'>`;
            products = form + products + '</div>';
            $('#content').html(products);
            bind_transfer(data.id);
         }
      })
   })
}

function bind_transfer(cards_id){
   $('#preTransferValues').keyup(function(event){
      elems = document.querySelectorAll('#transfer');
      if (event.keyCode == 13 && elems.length != 0){
         $(this).val($(this).val() + ', ');
      }
      return false;
   })

   $('#content').unbind('click').delegate('#transfer', 'click', function(){
      $("#transfer").prop("disabled", true);
      carPlate = document.forms.transferForm.carPlate.value;
      destination = document.forms.transferForm.destination.value;
      account = document.forms.transferForm.account.value;
      object = {
         "destination": destination,
         "carPlate": carPlate,
         "billNumber": account,
         "positions": cards_id
      };
      console.log(JSON.stringify(object));
      $.ajax({
         type: 'POST',
         url: `http://5.200.47.32:80/api/manufacture/transfer`,
         contentType: "application/json; charset=utf-8",
         data: JSON.stringify(object),
         success: function(data, textStatus)
         {
            window.open(`http://www.ferro-trade.ru/api/transferDocs/${data}`, "_blank");
            console.log(data);
            $('#content').html('');
         },
         error: function(data, textStatus)
         {
            console.log(data);
            if (textStatus === 400){
               $('.transferError').fadeIn();
               setTimeout(function (){
                  $('.transferError').fadeOut();
               }, 2000);
            }
         }
      })
   })
}

 $('#controlBtns').delegate("#addMany", "click", function(){
    $('.addManyForm').css('display','inline');
 });

 $('#controlBtns').delegate('#transfer', "click", function(){
    $('#content').html(
       `<form name="preTransferForm" class="col-12 preTransferForm">
          <div class="row">
             <div class="col-9 ui-widget">
                <input class="form-control col-10" name="preTransferValues" id="preTransferValues"/>
             </div>
             <div class="col-3">
                Вес: <span class="massInfo">0</span>
             </div>
          </div>
          <div class="row">
             <button type="button" class="btn btn-outline-info btn-block mb-2 ship" id="preTransfer">Трансфер</button>
          </div>
       </form>
       <div class="preTransferContent col-12 row"></div>`);
       bind_preTransfer();
 })

$('.addManyForm').delegate('.close-block-regMany', 'click', function(){
   $('.addManyForm').fadeOut();
})

 function CodetoPrint(id)
 {
    str = '';
    gost = '';
    console.log('works');
    $.ajax({
       type: 'GET',
       url: `http://5.200.47.32/api/manufacture/position/${id}`,
       dataType: 'json',
       async: false,
       success: function(data, textStatus)
       {
          console.log(data);
          mark = data.mark.replaceAll('–', '-');
          $.ajax({
             type: 'GET',   
             url: `http://5.200.47.32/api/gost?mark=${mark}`,
             async: false,
             success: function(data, textStatus)
             {
                console.log(data);
                gost = data;
             }
          })
          str = "<html><title>Штрих-код</title><head><scri"+
          "pt>function step1(){\n" +
          "setTimeout('step2()', 10);}\n" +
          "function step2(){window.print();window.close()}\n" +
          "</scri" + "pt></head><body onload='step1()'>\n" +
          "<img width='280' height='100' style='margin: auto;' src='" +
          `http://5.200.47.32/api/code/${id}` + `' />
           <p style='font-size: 20px; margin: 0;'>${data.manufacturer}</p>
           <p style='font-size: 20px; margin: 0;'>Марка: ${data.mark}</p>
           <p style='font-size: 20px; margin: 0;'>${gost}</p>
           <p style='font-size: 20px; margin: 0;'>Диаметр: ${data.diameter}</p>
           <p style='font-size: 20px; margin: 0;'>Упаковка: ${data.packing}</p>
           <p style='font-size: 20px; margin: 0;'>Плавка: ${data.plav}</p>
           <p style='font-size: 20px; margin: 0;'>Партия: ${data.part}</p>
           <p style='font-size: 20px; margin: 0;'>Масса: ${data.mass}</p></body></html>
          `;
          console.log(str);
       },
       error: function(data, textStatus){
          console.log(data);
       }
    })
    console.log(str);
    return str;
 }
 
 function PrintCode()
 {
    console.log(arguments);
    prints = '';
    for (i = 0; i < arguments.length; i++){
       prints += CodetoPrint(arguments[i]);
       console.log(prints);
    }
    var Pagelink = "";
    var pwa = window.open(Pagelink, "_blank");
    pwa.document.open();
    //console.log(ImagetoPrint(id));
    pwa.document.write(prints);
    pwa.document.close();
 }
 
 function PackageToPrint(id)
 {
    str = '';
    gost = '';
    console.log('works');
    $.ajax({
       type: 'GET',
       url: `http://5.200.47.32/api/manufacture/getById/${id}`,
       dataType: 'json',
       async: false,
       success: function(data, textStatus)
       {
          console.log(data);
          mark = data.mark.replaceAll('–', '-');
          $.ajax({
             type: 'GET',   
             url: `http://5.200.47.32/api/gost?mark=${mark}`,
             async: false,
             success: function(data, textStatus)
             {
                console.log(data);
                gost = data;
             }
          })
          str = "<html><title>Штрих-код</title><head><scri"+
              "pt>function step1(){\n" +
              "setTimeout('step2()', 10);}\n" +
              "function step2(){window.print();window.close()}\n" +
              "</scri" + "pt></head><body onload='step1()'>\n" +
              "<img width='280' height='100' style='margin: auto;' src='" +
              `http://5.200.47.32/api/code/${id}` + `' />
           <p style='font-size: 20px; margin: 0;'>${data.manufacturer}</p>
           <p style='font-size: 20px; margin: 0;'>Марка: ${data.mark}</p>
           <p style='font-size: 20px; margin: 0;'>${gost}</p>
           <p style='font-size: 20px; margin: 0;'>Диаметр: ${data.diameter}</p>
           <p style='font-size: 20px; margin: 0;'>Упаковка: ${data.packing}</p>
           <p style='font-size: 20px; margin: 0;'>Плавка: ${data.plav}</p>
           <p style='font-size: 20px; margin: 0;'>Партия: ${data.part}</p>
           <p style='font-size: 20px; margin: 0;'>Масса: ${data.mass}</p></body></html>
          `;
          console.log(str);
       },
       error: function(data, textStatus){
          console.log(data);
       }
    })
    console.log(str);
    return str;
 }
 
 function PrintPackage()
 {
    console.log(arguments);
    prints = '';
    for (i = 0; i < arguments.length; i++){
       prints += PackageToPrint(arguments[i]);
       console.log(prints);
    }
    var Pagelink = "";
    var pwa = window.open(Pagelink, "_blank");
    pwa.document.open();
    //console.log(ImagetoPrint(id));
    pwa.document.write(prints);
    pwa.document.close();
 }