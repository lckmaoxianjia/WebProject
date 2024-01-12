const mutations = {
    LOGIN: (state, value) => {
        localStorage.setItem('username', value)
        state.username = value
    },
    LOGOUT: (state,value) => {
        localStorage.removeItem('username')
        state.username = null
    }
}

export default mutations