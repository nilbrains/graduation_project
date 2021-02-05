const getters = {
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  islogin: state => state.user.islogin,
  uid: state => state.user.uid
}
export default getters
