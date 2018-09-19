package kr.co.anonymous_evcar.evcar.data;



    public class Member {
        private Long id;
        private String name;
        private String mem_id;
        private String mem_pw;
        private Integer gender;
        private String phone;
        private String loaction;
        private String email;


        public Member() {

        }


        public Member(Long id, String name, String mem_id, String mem_pw, Integer gender, String phone, String loaction, String email) {
            super();
            this.id = id;
            this.name = name;
            this.mem_id = mem_id;
            this.mem_pw = mem_pw;
            this.gender = gender;
            this.phone = phone;
            this.loaction = loaction;
            this.email = email;
        }

        public Member(String name, String mem_id, String mem_pw, Integer gender, String phone, String loaction, String email) {
            this.name = name;
            this.mem_id = mem_id;
            this.mem_pw = mem_pw;
            this.gender = gender;
            this.phone = phone;
            this.loaction = loaction;
            this.email = email;
        }

        public Long getId() {
            return id;
        }


        public void setId(Long id) {
            this.id = id;
        }


        public String getName() {
            return name;
        }


        public void setName(String name) {
            this.name = name;
        }


        public String getMem_id() {
            return mem_id;
        }


        public void setMem_id(String mem_id) {
            this.mem_id = mem_id;
        }


        public String getMem_pw() {
            return mem_pw;
        }


        public void setMem_pw(String mem_pw) {
            this.mem_pw = mem_pw;
        }


        public Integer getGender() {
            return gender;
        }


        public void setGender(Integer gender) {
            this.gender = gender;
        }


        public String getPhone() {
            return phone;
        }


        public void setPhone(String phone) {
            this.phone = phone;
        }


        public String getLoaction() {
            return loaction;
        }

        public void setLoaction(String loaction) {
            this.loaction = loaction;
        }

        public String getEmail() {
            return email;
        }


        public void setEmail(String email) {
            this.email = email;
        }


    }