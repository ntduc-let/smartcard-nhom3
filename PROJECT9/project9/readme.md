# PROJECT9 - Xây dựng thẻ cán bộ nhân viên sử dụng Smart card
Author: [Group9](http://facebook.com/xuanbinh.vu.6464) :scorpius:	
> Version 0.1 08/06/2021 :+1:
1. [ Mô tả chức năng dự kiến](#mô-tả-chức-năng-dự-kiến)
2. [ Mô tả chi tiết](#mô-tả-chi-tiết-các-chức-năng)
3. [ Demo Code](#demo-code)
***
***
### Mô tả chức năng dự kiến: 
* **Có cơ chế xác thực thẻ bằng mã PIN, mã PIN có thể thay đổi, có giới hạn số lần nhập sai cho phép**
* **Lưu thông tin cá nhân của chủ thẻ (mã số, họ tên, ngày sinh, tên cơ quan, ảnh cá nhân, số điện thoại…) Các thông tin trước khi lưu phải được mã hóa (sử dụng AES với khóa được tạo ra từ mã PIN). Có thể thay đổi thông tin người dùng.**
* **Có chức năng điểm danh khi đến và khi về. Khi điểm danh xác thực bằng mật mã khóa công khai (RSA hoặc ECC). Có chức năng mở cửa cơ quan sử dụng mật mã khóa công khai (RSA hoặc ECC)**
* **Xây dựng phần mềm giao tiếp với thẻ trên máy tính để demo các chức năng**
---
### Mô tả chi tiết các chức năng:
1. **Cơ chế xác thực thẻ bằng mã pin - VERIFY_PIN**
  * Lưu trạng thái đăng nhập
  * <sau khi kết nối thẻ, yêu cầu VERIFY_PIN nếu nhập sai quá số lần cho phép sẽ khoá thẻ>
2. **Thay đổi mã pin - CHANGE_PIN**
  * Sau khi đăng nhập thẻ thành công có thể chọn chức năng thay đổi mã PIN
  * Yêu cầu nhập mã pin cũ và mới (khoá thẻ nếu số lần nhập mã pin cũ vượt quá số lần cho phép)
3. **Giới hạn số lần nhập sai đối với từng mã pin**
  * Khi *CREATE_PIN* sẽ truyền thêm tham số là số lần nhập sai cho phép
  * Các pin khi bị khoá sẽ lưu lại phục vụ cho mở khoá thẻ và thay đổi pin (không trùng với mã pin cũ)
4. **Lưu thông tin cá nhân của chủ thẻ**
  * Tạo thông tin cá nhân cho lần sử dụng thẻ đầu tiên
  * > Sử dụng truyền dữ liệu lớn
5. **Cho phép thay đổi thông tin cá nhân**
  * Sau khi đăng nhập thẻ thành công cho phép chọn chức năng thay đổi thông tin cá nhân
  * Hiển thị thông tin cũ và cho phép cập nhật thông tin mới
6. **Mã hoá thông tin trước khi lưu xuống thẻ -  sử dụng khoá AES**
7. **Chức năng điểm danh**
 * Có chức năng điểm danh khi đến và về
 * Xác thực bằng mã khoá công khai ECC
9. **Dựng phần mềm giao tiếp trên máy tính**
---
### Demo code
> Version 0.1

- [x] VERIFY_PIN
- [x] CREATE_PIN
- [x] CHANGE_PIN
- [x] UNBLOCK_PIN
- [ ] CREATE_INFORMATION
- [ ] CHANGE_INFORMATION
- [ ] ENCRYPTION_AES
- [ ] STAFF_PRESENCE

#### Chi tiết
1 VERIFY_PIN

> Biến ghi lại trạng thái đăng nhập
```javascript
private short logged_ids;
```

> Funtion Verify_Pin (không thành công -> return lỗi | thành công -> *logged_ids* )
```javascript
	private void VerifyPIN(APDU apdu, byte[] buffer) {
		//pin_nb: th t ca pin trong ng các pin
		// byte pin_nb = buffer[ISO7816.OFFSET_P1];
		
		byte pin_nb = (byte)0;
		if ((pin_nb < 0) || (pin_nb >= MAX_NUM_PINS))
			ISOException.throwIt(SW_INCORRECT_P1);
		OwnerPIN pin = pins[pin_nb];
		if (pin == null)
			ISOException.throwIt(SW_INCORRECT_P1);
		if (buffer[ISO7816.OFFSET_P2] != 0x00)
			ISOException.throwIt(SW_INCORRECT_P2);
		short numBytes = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		
		if (numBytes != apdu.setIncomingAndReceive())
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		if (!CheckPINPolicy(buffer, ISO7816.OFFSET_CDATA, (byte) numBytes))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (pin.getTriesRemaining() == (byte) 0x00)
			ISOException.throwIt(SW_IDENTITY_BLOCKED);
		if (!pin.check(buffer, (short) ISO7816.OFFSET_CDATA, (byte) numBytes)) {
			LogoutIdentity(pin_nb);
			ISOException.throwIt(SW_AUTH_FAILED);
		}
		logged_ids |= (short) (0x0001 << pin_nb);
	}
```
- Về phép dịch trái bit để ghi trạng thái đăng nhập ```logged_ids |= (short) (0x0001 << pin_nb);```

 ```logged_ids``` ban đầu bằng ```NULL```
 ```pin_nb``` là thứ tự của pin trong list các pin (trong code DEFINE 0-7 <tối đa có 8 mã pin được lưu>)
 
 trường hợp hiện tại chỉ sử dụng 1 mã pin là pins[0]
 ```(short) (0x0001 << pin_nb) = (short) (0x0001)``` => ```logged_ids = 0x0001```

 trường hợp có nhiều mã pin và đang sử dụng mã pins[1]
 ```(short) (0x0001 << pin_nb) = (short) (0x0010)``` => ```logged_ids = 0x0001 | 0x0010 = 0x0011```
 
 mục đích sử dụng là để kiểm tra đang đăng nhập bằng mã pin nào trong list pin phục vụ cho khoá pin và thay đổi pin không được phép trùng với pin cũ
