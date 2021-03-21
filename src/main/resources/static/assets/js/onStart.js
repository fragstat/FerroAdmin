$(document).ready(onStart());

function stopIt()
{
    $(document).on("keypress", 'form', function (e) {
       var code = e.keyCode || e.which;
       if (code == 13) {
           e.preventDefault();
           return false;
       }
    });
}

function onStart()
{
    console.log('works');
    stopIt();
   $('.container-fluid').html(`<div class="row updateForm">
    <div class="col-md-12">
      <div class="col-12 text-center">
        <h5 class="text-center row"><span class="col-11">Обновить продукт</span><span class="close-block-update col-1">&#9746;</span></h5>
      </div>
      <div class="col-12">
        <form name="updForm">
          <div class="form-group">
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="updMark">Марка</label>
                <input type="text" name="updMark" class="form-control ui-autocomplete-input" id="updMark" placeholder="" value="" required="" autocomplete="off">
              </div>
              <div class="col-md-6 mb-3">
                <label for="updDiameter">Диаметр</label>
                <input class="form-control" name="updDiameter" id="updDiameter"/>
              </div>
            </div>
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="updPacking">Упаковка</label>
                <select class="form-control" name="updPacking" id="updPacking">
                  <option>Моток</option>
                  <option>К200</option>
                  <option>Д200</option>
                  <option>К300</option>
                  <option>Д300</option>
                  <option>К415</option>
                  <option>Пруток(600мм)</option>
                  <option>Пруток(1000мм)</option>
                </select>
              </div>
              <div class="col-md-6 mb-3">
                <label for="updPlav">Плавка</label>
                <input class="form-control" name="updPlav" id="updPlav"/>
              </div>
            </div>
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="updPart">Партия</label>
                <input class="form-control" name="updPart" id="updPart"/>
              </div>
              <div class="col-md-6 mb-3">
                <label for="updMass">Вес</label>
                <input class="form-control" name="updMass" id="updMass"/>
              </div>
            </div>
            <div class="row">
              <div class="col-md-6">
                <label for="updManufacturer">Производитель</label>
                <input type="text" name="updManufacturer" class="form-control ui-autocomplete-input" id="updManufacturer" oninput='help_man();' placeholder="" value="" required="" autocomplete="off">
              </div>
              <div class="col-md-3 row mb-3">
                <label for="updId">ID</label>
                <input type="text" name="updId" class="form-control" id="updId" />
              </div>
              <button type="button" class="btn btn-outline-success btn-block col-md-2 m-4" id="loadInfoById">Подгрузить</button>
            </div>
            <div class="row">
              <label for="updComment">Комментарий</label>
              <textarea class="form-control" id="updComment" name="updComment" rows="3" style="margin-top: 0px; margin-bottom: 0px; height: 79px;"></textarea>
            </div>
          </div>
        </form>
        <button type="button" class="btn btn-outline-success btn-block" id="upd">Обновить</button>
      </div>
    </div>
  </div>
    <div class="row form">
        <div class="col-md-12">
            <div class="col-12 text-center">
                <h5 class="text-center row"><span class="col-11">Зарегистрировать продукт</span><span
                        class="close-block col-1">&#9746;</span></h5>
            </div>
            <div class="col-12">
                <form name="regForm">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="mark">Марка</label>
                                <input autocomplete="off" class="form-control ui-autocomplete-input" id="mark"
                                       name="mark" placeholder="" required="" type="text" value="">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="diameter">Диаметр</label>
                                <input class="form-control" id="diameter" name="diameter"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="packing">Упаковка</label>
                                <select class="form-control" id="packing" name="packing">
                                    <option>Моток</option>
                                    <option>К200</option>
                                    <option>Д200</option>
                                    <option>К300</option>
                                    <option>Д300</option>
                                    <option>К415</option>
                                    <option>Пруток(600мм)</option>
                                    <option>Пруток(1000мм)</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="plav">Плавка</label>
                                <input class="form-control" id="plav" name="plav"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="part">Партия</label>
                                <input class="form-control" id="part" name="part"/>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="mass">Вес</label>
                                <input class="form-control" id="mass" name="mass"/>
                            </div>
                        </div>
                        <div class="row">
                            <label for="manufacturer">Производитель</label>
                            <input autocomplete="off" class="form-control ui-autocomplete-input" id="manufacturer"
                                   name="manufacturer" oninput='help_man();' placeholder="" required="" type="text"
                                   value="">
                        </div>
                        <div class="row">
                            <label for="comment">Комментарий</label>
                            <textarea class="form-control" id="comment" name="comment" rows="3"
                                      style="margin-top: 0px; margin-bottom: 0px; height: 79px;"></textarea>
                        </div>
                    </div>
                </form>
                <button class="btn btn-outline-success btn-block" id="add">Зарегистрировать</button>
            </div>
        </div>
    </div>
    <div class="row addManyForm">
        <div class="col-md-12">
            <div class="col-12 text-center">
                <h5 class="text-center row"><span class="col-11">Зарегистрировать продукты</span><span
                        class="close-block-regMany col-1">&#9746;</span></h5>
            </div>
            <div class="col-12">
                <form name="regManyForm">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                        <span class="row pl-3">
                          <label for="mark">Марка</label>
                          <input class="addManyFormCheck mt-2 ml-2" id="mark" name="addManyFormCheck" type="checkbox">
                        </span>
                                <input autocomplete="off" class="form-control ui-autocomplete-input" id="markMany"
                                       disabled name="markMany" placeholder="" required="" type="text" value="">
                            </div>
                            <div class="col-md-6 mb-3">
                        <span class="row pl-3">
                          <label for="diameter">Диаметр</label>
                          <input class="addManyFormCheck mt-2 ml-2" id="diameter" name="addManyFormCheck"
                                 type="checkbox">
                        </span>
                                <input class="form-control" disabled id="diameterMany" name="diameterMany"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                        <span class="row pl-3">
                          <label for="packing">Упаковка</label>
                          <input class="addManyFormCheck mt-2 ml-2" id="packing" name="addManyFormCheck"
                                 type="checkbox">
                        </span>
                                <select class="form-control" disabled id="packingMany" name="packingMany">
                                    <option>Моток</option>
                                    <option>К200</option>
                                    <option>Д200</option>
                                    <option>К300</option>
                                    <option>Д300</option>
                                    <option>К415</option>
                                    <option>Пруток(600мм)</option>
                                    <option>Пруток(1000мм)</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                        <span class="row pl-3">
                          <label for="plav">Плавка</label>
                          <input class="addManyFormCheck mt-2 ml-2" id="plav" name="addManyFormCheck" type="checkbox">
                        </span>
                                <input class="form-control" disabled id="plavMany" name="plavMany"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                        <span class="row pl-3">
                          <label for="part">Партия</label>
                          <input class="addManyFormCheck mt-2 ml-2" id="part" name="addManyFormCheck" type="checkbox">
                        </span>
                                <input class="form-control" disabled id="partMany" name="partMany"/>
                            </div>
                            <div class="col-md-6 mb-3">
                        <span class="row pl-3">
                          <label for="mass">Вес</label>
                          <input class="addManyFormCheck mt-2 ml-2" id="mass" name="addManyFormCheck" type="checkbox">
                        </span>
                                <input class="form-control" disabled id="massMany" name="massMany"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-9 mb-3">
                        <span class="row pl-3">
                          <label for="manufacturer">Производитель</label>
                          <input class="addManyFormCheck mt-2 ml-2" id="manufacturer" name="addManyFormCheck"
                                 type="checkbox">
                        </span>
                                <input autocomplete="off" class="form-control ui-autocomplete-input"
                                       id="manufacturerMany" name="manufacturerMany" oninput='help_man();'
                                       disabled placeholder="" required="" type="text" value="">
                            </div>
                            <div class="col-3">
                                <label for="number">Кол-во</label>
                                <input class="form-control" id="number" name="number" type="number" value="1">
                            </div>
                        </div>
                        <div class="row">
                      <span class="row pl-3">
                        <label for="comment">Комментарий</label>
                        <input class="addManyFormCheck mt-2 ml-2" id="comment" name="addManyFormCheck" type="checkbox">
                      </span>
                            <textarea class="form-control" id="commentMany" name="commentMany" rows="3"
                                      disabled style="margin-top: 0px; margin-bottom: 0px; height: 79px;"></textarea>
                        </div>
                    </div>
                </form>
                <button class="btn btn-outline-success btn-block" id="addMany">Зарегистрировать</button>
            </div>
        </div>
    </div>` + $(".container-fluid").html());
  $( function() {
    var availableTags = [
      "08Х18Н10", "10Х17Н13М2Т", "12Х13", "12Х13-Т", "12Х13-Т-1", "12Х18Н10Т", "12Х18Н10Т-В", "12Х18Н10Т-ВО", "12Х18Н10Т-Н", "12Х18Н9", "20Х13", "20Х23Н18", "20Х25Н20С2", "Нп-13Х15АГ13ТЮ", "Нп-30ХГСА", "Св-01Х19Н9", "Св-01Х23Н28М3Д3Т", "Св-03Х3МД", "Св-03ХН3МД", "Св-04Н2ГСТА", "Св-04Н3ГСМТА", "Св-04Н3ГСМТА-Ш", "Св-04Н3ГМТА", "Св-04Н3ГМТА", "Св-04Х17Н10М2", "СВ-04Х19Н11М3", "Св-04Х20Н10Г2Б", "Св-04Х19Н9", "Св-04Х19Н9С2", "Св-04Х2МА", "Св-05Х19Н9Ф3С2", "Св-05Х20Н9ФБС", "Св-06Н3", "Св-06Х14", "Св-06Х15Н60М15", "Св-06Х19Н10М3Т", "Св-06Х19Н9Т", "Св-06Х20Н11М3ТБ", "Св-06Х25Н12ТЮ", "Св-06ХН28МДТ", "Св-07Х16Н6", "Св-07Х18Н9ТЮ", "Св-07Х19Н10Б", "Св-07Х25Н12Г2Т", "Св-07Х25Н13", "Св-08", "Св-08А", "Св-08АА", "Св-08Г2С", "Св-08Г2С-О", "Св-08Г2С-П", "Св-08ГС-О","Св-08ГС-П", "Св-08ГА", "Св-08ГНМ", "Св-08ГС", "Св-08ГСМТ", "Св-08ГСМТ-О", "Св-08ГСНТ", "Св-08ГСНТА", "Св-08ГН2СМД", "Св-08МХ", "Св-08Н50", "Св-08Х14ГНТ", "Св-08Х16Н8М2", "Св-08Х18Н8Г2Б", "Св-08Х19Н10Г2Б", "Св-08Х19Н10М3Б", "Св-08Х19Н11Ф2С2", "Св-08Х19Н9Ф2С2", "Св-08Х20Н9Г7Т", "Св-08Х21Н10Г6", "Св-08Х21Н11ФТ", "Св-08Х25Н13БТЮ", "Св-08Х25Н40М7", "Св-08Х25Н60М10", "Св-08Х3Г2СМ", "Св-08ХГ2С", "Св-08ХГСМА", "Св-08ХМ-О", "Св-08ГСНТ-О", "Св-08ХГСМФА", "Св-08ХГСМФА-О", "Св-08ХГСМФА-П", "Св-08ХМ", "Св-08ХМНФБА", "Св-08ХМФА", "Св-08ХМФА-О", "Св-08ХН2Г2СМЮ", "Св-08ХН2ГМТА", "Св-08ХН2ГМЮ", "Св-08ХН2М", "Св-08ХНМ", "Св-09Х16Н25М6АФ", "Св-09Х16Н4Б", "Св-10Г2", "Св-10Г2-О", "Св-10ГА","Св-10ГСМТ", "Св-10ГСМТ-О", "Св-10ГН", "Св-10ГН1МА", "Св-10ГНА", "Св-10ГНМА", "Св-10НМА", "Св-10НМА-О", "Св-10НЮ", "Св-10Х11НВМФ", "Св-10Х14Г14Н4Т", "Св-10Х16Н25АМ6", "Св-10Х16Н25М6АФ", "Св-10Х17Т", "Св-10Х19Н11М4Ф", "Св-10Х20Н15", "Св-10Х5М", "Св-10ХГ2СМА", "Св-08ГСМТ", "Св-08ГСМТ-О", "Св-10ХМФТ", "Св-10ХН2ГМТ", "Св-12ГС", "Св-12Х13", "Св-12Х13-Т", "Св-12Х21Н5Т", "Св-12X11НМФ", "Св-13Х25Н18", "Св-13Х25Т", "Св-13Х2МФТ", "Св-15ГСТЮЦА", "Св-15Х18Н12С4Ю", "Св-15Х18Н12С4ТЮ-ТО", "Св-18ХГС", "Св-18ХМА", "Св-18ХМА-О", "Св-20ГСТЮА", "Св-20Х13", "Св-30Х15Н35В3Б3Т", "Св-30Х25Н16Г7"
    ];
    $( "#mark" ).autocomplete({
      source: availableTags
    });
    $('#markMany').autocomplete({source: availableTags});
  } );
}
