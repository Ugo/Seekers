function GetMember() {
    $('input[type=button]').attr('disabled', true);
    $('.average-id').html('');
    $('.average-content').html('');
    $("#MemberDetails").addClass("loading");
    $.ajax({
		type: "GET",
	    url: "http://localhost:8080/average?content=" + $("#numberPrice").val(),
		contentType: "application/json; charset=utf-8",
        dataType: "json"
	}).then(function(data) {
		$('.average-id').append(data.id);
		$('.average-content').append(data.content);
		$('input[type=button]').attr('disabled', false);
	});
}
