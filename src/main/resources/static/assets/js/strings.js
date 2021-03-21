function table(data){
    products = `<div class="table-responsive"><table class="table table-sm">
    <thead>
    <tr>
       <th scope="col">#</th>
       <th scope="col">Марка</th>
       <th scope="col">Диаметр</th>
       <th scope="col">Упаковка</th>
       <th scope="col">Масса</th>
    </tr>
    </thead>
    <tbody>`;
    for (i = 0; i < data.length; i++) {
        products += `<tr><th>${i + 1}</th><td>${data[i].mark}</td><td>${data[i].diameter}</td>
       <td>${data[i].packing}</td><td>${data[i].mass.toFixed(2)}</td></tr>`;
    }
    products += '</tbody></table></div>';
    return products;
}

function table_for_history(data){
    products = `<div class="table-responsive"><table class="table table-sm">
   <thead>
   <tr>
      <th scope="col">Номер отгрузки</th>
      <th scope="col">Номер счёта</th>
      <th scope="col">Агент</th>
      <th scope="col">Дата</th>
      <th scope="col">Масса</th>
   </tr>
   </thead>
   <tbody>`;
    for (i = 0; i < data.length; i++){
        products += `<tr onclick="openShipmentHistory(${data[i].id})"><th>${data[i].id}</th><td>${data[i].bill}</td><td>${data[i].contrAgent}</td>
      <td>${data[i].date}</td><td>${data[i].mass.toFixed(2)}</td></tr>`;
    }
    products += '</tbody></table></div>';
    return products;
}

function cards(data) {
    products = '';
    data.forEach(function (elem) {
        if (elem.status == 'Departured')
            status = 'Отгружен';
        else if (elem.status == 'In_stock')
            status = 'На складе';
        if (elem.comment == null || elem.comment == "") {
            products = products + card_without_comm(elem.id, elem.mark, elem.diameter, elem.packing, elem.part, elem.plav, elem.mass, status);
        }
        else{
            products = products + card(elem.id, elem.mark, elem.diameter, elem.packing, elem.comment, elem.part, elem.plav, elem.mass, status);
        }
    });
    return products;
}

function card(id, mark, diameter, packing, comment, part, plav, mass, status) {
    return `<div class="col-md-4 col-sm-6 col-6 p-1"><a href="product?id=${id}" class="card product col-12 p-0" id="${id}">
     <div class="card-body p-1">
         <div class="col-12 text-center">Позиция</div>
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Сплав: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Статус: ${status}</p>
            <p class="card-text">Комментарий: ${comment}</p>
        </div>
    </a></div>`;
}

function card_without_comm(id, mark, diameter, packing, part, plav, mass, status) {
    return `<div class="col-md-4 col-sm-6 col-6 p-1"><a href="product?id=${id}" class="card product col-12 p-0" target="_blank" id="${id}">
         <div class="card-body p-1">
             <div class="col-12 text-center">Позиция</div>
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Сплав: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Статус: ${status}</p>
        </div>
    </a></div>`;
}

function pack(id, mark, diameter, packing, comment, part, plav, mass, status) {
    return `<div class="col-md-4 col-sm-6 col-6 p-1"><a href="package?id=${id}" class="card product col-12 p-0" target="_blank" id="${id}">
         <div class="card-body p-1"> 
             <div class="col-12 text-center">Поддон</div>
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Плавка: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Статус: ${status}</p>
            <p class="card-text">Комментарий: ${comment}</p>
        </div>
    </a></div>`;
}

function pack_without_comm(id, mark, diameter, packing, part, plav, mass, status) {
    return `<div class="col-md-4 col-sm-6 col-6 p-1"><a href="package?id=${id}" class="card product col-12 p-0" target="_blank" id="${id}">
         <div class="card-body p-1"> 
             <div class="col-12 text-center">Поддон</div>
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Плавка: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Статус: ${status}</p>
        </div>
    </a></div>`;
}

function card_depart(id, mark, diameter, packing, part, plav, mass, status) {
    return `<div class="col-md-4 col-sm-6 col-6 p-1"><div class="card product" id="${id}">
        <div class="card-body"> 
            <p class="card-text">Марка: ${mark}</p>
            <p class="card-text">Диаметр: ${diameter}</p>
            <p class="card-text">Упаковка: ${packing}</p>
            <p class="card-text">Партия: ${part}</p>
            <p class="card-text">Плавка: ${plav}</p>
            <p class="card-text">Вес: ${mass}</p>
            <p class="card-text">Статус: ${status}</p>,
            <label for="mass">Масса отгрузки</label>
            <input class="mass form-control col-10" name="mass" id="mass ${id}"/>
        </div>
    </div></div>`;
}

function card_to_print(id) {
    str = '';
    $.ajax({
        type: 'GET',
        url: `http://5.200.47.32/api/position/${id}`,
        dataType: 'json',
        async: false,
        success: function (data, textStatus) {
            mark = data.mark;
            diameter = data.diameter;
            packing = data.packing;
            part = data.part;
            plav = data.plav;
            mass = data.mass;
            str += `
         <div class="card product mx-1 my-1" style="width:30%" id="${id}">
            <div class="card-body"> 
               <p class="card-text">Марка: ${mark}</p>
               <p class="card-text">Диаметр: ${diameter}</p>
               <p class="card-text">Упаковка: ${packing}</p>
               <p class="card-text">Партия: ${part}</p>
               <p class="card-text">Плавка: ${plav}</p>
               <p class="card-text">Вес: ${mass}</p>
               <button class="btn btn-outline-info btn-block mb-2" id="print" onclick='PrintCode(${id}); return false;'>Печать</button>
            </div>
         </div>`;
        },
        error: function () {
            console.log(data, textStatus);
        }
    })
    return str;
}

function card_for_many_reg(checkMark, checkDiameter, checkPacking, checkPart, checkPlav, checkMass, checkManufacturer, checkComment, mark, diameter, packing, part, plav, mass, manufacturer, comment) {
    card = `<div class="col-md-4 col-sm-6 col-6 p-1"><div class="card productInput"><div class="card-body p-1">`;
    if (checkMark)
        card += `<p class="card-text">Марка: ${mark}</p>`;
    else
        card += `<div class="row col-12">
      <label for="mark">Марка</label>
      <input class="form-control" name="mark" id="mark" />
      </div>`;
    if (checkDiameter)
        card += `<p class="card-text">Диаметр: ${diameter}</p>`;
    else
        card += `<div class="row col-12">
      <label for="diameter">Диаметр</label>
      <input class="form-control" name="diameter" id="diameter" />
      </div>`;
    if (checkPacking)
        card += `<p class="card-text">Упаковка: ${packing}</p>`;
    else
        card += `<div class="row col-12">
      <label for="packing">Упаковка</label>
      <input class="form-control" name="packing" id="packing" />
      </div>`;
    if (checkPart)
        card += `<p class="card-text">Партия: ${part}</p>`;
    else
        card += `<div class="row col-12">
      <label for="part">Партия</label>
      <input class="form-control" name="part" id="part" />
      </div>`;
    if (checkPlav)
        card += `<p class="card-text">Плавка: ${plav}</p>`;
    else
        card += `<div class="row col-12">
      <label for="plav">Плавка</label>
      <input class="form-control" name="plav" id="plav" />
      </div>`;
    if (checkMass)
        card += `<p class="card-text">Вес: ${mass}</p>`;
    else
        card += `<div class="row col-12">
      <label for="mass">Вес</label>
      <input class="form-control regManyMass" name="mass" id="mass" onchange="addManyOnChange(); return false;"/>
      </div>`;
    if (checkManufacturer)
        card += `<p class="card-text">Производитель: ${manufacturer}</p>`;
    else
        card += `<div class="row col-12">
      <label for="manufacturer">Производитель</label>
      <input class="form-control" name="manufacturer" id="manufacturer" />
      </div>`;
    if (checkComment)
        card += `<p class="card-text">Комментарий: ${comment}</p>`;
    else
        card += `<div class="row col-12">
      <label for="comment">Комментарий</label>
      <input class="form-control" name="comment" id="comment" />
      </div>`;
    return card + `</div></div></div>`;
}