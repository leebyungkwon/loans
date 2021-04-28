    @PostMapping("/login")
    public ResponseEntity<ResponseMsg> login(ManagerMngDomain mem,
                          HttpServletRequest req,
                          HttpServletResponse res) {
        ResponseMsg responseMsg = new ResponseMsg(HttpStatus.OK, "");
        ManagerMngDomain member = managerMngService.login(mem.getLoginId(), mem.getPwd());
    	if(member ==  null) {
    		new ResponseMsg(HttpStatus.BAD_REQUEST, "COM0002");
    		return new ResponseEntity<ResponseMsg>(responseMsg, HttpStatus.BAD_REQUEST);
    	}

        String date = null;
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        date = sdformat.format(cal.getTime());
        SessionDomain session = new SessionDomain();
        session.setId(member.getId());
        session.setExpireDate(date);
        final String token = UtilJwtToken.createToken(member.getLoginId(), session, false);

        utilRedis.setData(token, session);
		/* String result = stringValueOperations.get(token); */

        res.addCookie(UtilCookie.createCookie("token",token));
		/* Cookie c = CookieUtil.getCookie(req, token); */

		return new ResponseEntity<ResponseMsg>(responseMsg, HttpStatus.OK);

    }
