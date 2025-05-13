// Current user data from JWT token
const currentUser = getCurrentUser() || {
    id: 1,
    username: 'johndoe',
    fullName: 'John Doe',
    profilePic: 'https://via.placeholder.com/150'
};

// Redirect to login if not authenticated
if (!currentUser) {
    window.location.href = '/login';
    throw new Error('User not authenticated');
}

// Sample data for stories (will be replaced with API call)
const stories = [];

// Sample data for posts (will be replaced with API call)
let posts = [];

// Sample data for suggestions (will be replaced with API call)
let suggestions = [];

// Initialize the page
document.addEventListener('DOMContentLoaded', function() {
    // Update UI with current user info
    updateUserInfo();
    
    // Load data
    loadStories();
    loadPosts();
    loadSuggestions();
    
    // Setup event listeners
    setupEventListeners();
    
    // Add logout functionality
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            localStorage.removeItem('jwtToken');
            window.location.href = '/login';
        });
    }
});

// Update UI with current user info
function updateUserInfo() {
    // Update navigation profile picture
    const navProfilePic = document.getElementById('navProfilePic');
    if (navProfilePic) navProfilePic.src = currentUser.profilePic;
    
    // Update post creator info
    const userAvatar = document.getElementById('userAvatar');
    if (userAvatar) userAvatar.src = currentUser.profilePic;
    
    const postUsername = document.getElementById('postUsername');
    if (postUsername) postUsername.textContent = currentUser.username;
    
    // Update sidebar user info
    const currentUserPic = document.getElementById('currentUserPic');
    if (currentUserPic) currentUserPic.src = currentUser.profilePic;
    
    const currentUsername = document.getElementById('currentUsername');
    if (currentUsername) currentUsername.textContent = currentUser.username;
    
    const currentFullName = document.getElementById('currentFullName');
    if (currentFullName) currentFullName.textContent = currentUser.fullName || currentUser.username;
}

// Load stories from API
async function loadStories() {
    try {
        const response = await fetchWithAuth('/api/v1/user/stories');
        const data = await response.json();
        stories = data;
        // const data = await response.json();
        // stories = data;
        
        // For now, use sample data
        const container = document.getElementById('storiesContainer');
        if (!container) return;
        
        container.innerHTML = `
            <div class="story" onclick="viewStory(${currentUser.id})">
                <div class="story-avatar">
                    <img src="${currentUser.profilePic}" alt="Your Story">
                </div>
                <div class="story-username">Your Story</div>
            </div>
        `;
        
        // Add other stories
        stories.forEach(story => {
            const storyElement = document.createElement('div');
            storyElement.className = 'story';
            storyElement.innerHTML = `
                <div class="story-avatar" onclick="viewStory(${story.id})">
                    <img src="${story.profilePic}" alt="${story.username}">
                </div>
                <div class="story-username">${story.username}</div>
            `;
            container.appendChild(storyElement);
        });
    } catch (error) {
        console.error('Error loading stories:', error);
    }
}

// Load posts from API
async function loadPosts() {
    try {
        const response = await fetchWithAuth('/api/v1/posts/feed');
        const data = await response.json();
        posts = data;
        
        const container = document.getElementById('postsContainer');
        if (!container) return;
        
        if (posts.length === 0) {
            container.innerHTML = `
                <div class="text-center py-5">
                    <h5>No posts yet</h5>
                    <p class="text-muted">Follow people to see their posts here</p>
                </div>
            `;
            return;
        }
        
        container.innerHTML = '';
        posts.forEach(post => {
            const postElement = document.createElement('div');
            postElement.className = 'post mb-4';
            postElement.setAttribute('data-post-id', post.id);
            postElement.innerHTML = `
                <div class="post-header">
                    <img src="${post.userProfilePic || 'https://via.placeholder.com/32'}" class="post-avatar" alt="${post.username}">
                    <div class="post-username">${post.username}</div>
                    <div class="post-more ms-auto">
                        <i class="fas fa-ellipsis-h"></i>
                    </div>
                </div>
                <div class="post-image">
                    <img src="${post.imageUrl}" class="w-100" alt="Post">
                </div>
                <div class="post-actions">
                    <a href="#" class="action-icon ${post.isLiked ? 'fas text-danger' : 'far'}" 
                       onclick="toggleLike(${post.id}, this); return false;">
                        <i class="fa-heart"></i>
                    </a>
                    <a href="#" class="action-icon" onclick="focusComment(${post.id}); return false;">
                        <i class="far fa-comment"></i>
                    </a>
                    <a href="#" class="action-icon">
                        <i class="far fa-paper-plane"></i>
                    </a>
                    <a href="#" class="action-icon ms-auto">
                        <i class="far fa-bookmark"></i>
                    </a>
                </div>
                <div class="post-likes">${post.likeCount || 0} likes</div>
                <div class="post-caption">
                    <span class="fw-bold">${post.username}</span> ${post.caption || ''}
                </div>
                ${post.commentCount > 0 ? `
                    <div class="post-comments" onclick="viewComments(${post.id})">
                        View all ${post.commentCount} comments
                    </div>
                ` : ''}
                <div class="post-time">${post.timeAgo || 'Just now'}</div>
                <div class="add-comment">
                    <input type="text" class="comment-input" placeholder="Add a comment..." 
                           onkeyup="handleCommentKeyup(event, ${post.id})">
                    <button class="post-btn" onclick="postComment(${post.id}, this)" disabled>Post</button>
                </div>
            `;
            container.appendChild(postElement);
        });
    } catch (error) {
        console.error('Error loading posts:', error);
    }
}

// Load user suggestions
async function loadSuggestions() {
    try {
        const response = await fetchWithAuth('/api/v1/user/suggestions');
        const data = await response.json();
        suggestions = data;
        
        const container = document.getElementById('suggestionsList');
        if (!container) return;
        
        if (suggestions.length === 0) {
            container.innerHTML = '<p class="text-muted small">No suggestions available</p>';
            return;
        }
        
        container.innerHTML = '';
        suggestions.forEach(user => {
            const suggestionElement = document.createElement('div');
            suggestionElement.className = 'suggestion';
            suggestionElement.innerHTML = `
                <img src="${user.profilePic || 'https://via.placeholder.com/32'}" class="suggestion-avatar" alt="${user.username}">
                <div class="suggestion-info">
                    <div class="suggestion-username">${user.username}</div>
                    <div class="suggestion-text">Followed by ${user.mutualFollowers} mutual friends</div>
                </div>
                <button class="follow-btn ${user.isFollowing ? 'following' : ''}" 
                        onclick="followUser(${user.id}, this); return false;">
                    ${user.isFollowing ? 'Following' : 'Follow'}
                </button>
            `;
            container.appendChild(suggestionElement);
        });
    } catch (error) {
        console.error('Error loading suggestions:', error);
    }
}

// Setup event listeners
function setupEventListeners() {
    // Search functionality
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(searchUsers, 300));
    }
    
    // Create post button
    const createPostBtn = document.getElementById('createPostBtn');
    const postCaption = document.getElementById('postCaption');
    
    if (postCaption) {
        postCaption.addEventListener('input', function() {
            if (createPostBtn) {
                createPostBtn.disabled = !this.value.trim();
            }
        });
    }
    
    // Add photo button
    const addPhotoBtn = document.getElementById('addPhotoBtn');
    const postImage = document.getElementById('postImage');
    
    if (addPhotoBtn && postImage) {
        addPhotoBtn.addEventListener('click', () => postImage.click());
        
        postImage.addEventListener('change', function() {
            if (this.files && this.files[0]) {
                // Handle image preview if needed
                console.log('Image selected:', this.files[0]);
                if (createPostBtn) {
                    createPostBtn.disabled = false;
                }
            }
        });
    }
    
    // Create post
    if (createPostBtn) {
        createPostBtn.addEventListener('click', createPost);
    }
}

// Create a new post
async function createPost() {
    const caption = document.getElementById('postCaption')?.value.trim() || '';
    const imageInput = document.getElementById('postImage');
    
    if (!caption && !(imageInput?.files?.[0])) {
        return;
    }
    
    try {
        const formData = new FormData();
        formData.append('caption', caption);
        if (imageInput?.files?.[0]) {
            formData.append('image', imageInput.files[0]);
        }
        
        const response = await fetchWithAuth('/api/v1/post', {
            method: 'POST',
            body: formData
        });
        
        if (!response.ok) {
            throw new Error('Failed to create post');
        }
        
        const post = await response.json();
        
        // Clear form
        if (document.getElementById('postCaption')) {
            document.getElementById('postCaption').value = '';
        }
        if (imageInput) {
            imageInput.value = '';
        }
        if (document.getElementById('createPostBtn')) {
            document.getElementById('createPostBtn').disabled = true;
        }
        
        // Show success message
        Swal.fire({
            icon: 'success',
            title: 'Post created!',
            showConfirmButton: false,
            timer: 1500
        });
        
        // Reload posts
        loadPosts();
        
    } catch (error) {
        console.error('Error creating post:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Failed to create post. Please try again.'
        });
    }
}

// Search users
async function searchUsers(event) {
    const query = event.target.value.trim();
    const resultsContainer = document.getElementById('searchResults');
    
    if (!query) {
        if (resultsContainer) {
            resultsContainer.style.display = 'none';
        }
        return;
    }
    
    try {
        const response = await fetchWithAuth(`/api/v1/user/search?keyword=${encodeURIComponent(query)}`);
        const users = await response.json();
        
        if (resultsContainer) {
            resultsContainer.innerHTML = '';
            
            if (users.length === 0) {
                resultsContainer.innerHTML = '<div class="p-3 text-muted">No users found</div>';
            } else {
                users.forEach(user => {
                    const userElement = document.createElement('div');
                    userElement.className = 'search-result-item';
                    userElement.innerHTML = `
                        <img src="${user.profilePic || 'https://via.placeholder.com/32'}" class="search-result-avatar" alt="${user.username}">
                        <span class="search-result-username">${user.username}</span>
                    `;
                    userElement.addEventListener('click', () => {
                        window.location.href = `/profile/${user.id}`;
                    });
                    resultsContainer.appendChild(userElement);
                });
            }
            
            resultsContainer.style.display = 'block';
        }
        
    } catch (error) {
        console.error('Error searching users:', error);
        if (resultsContainer) {
            resultsContainer.innerHTML = '<div class="p-3 text-muted">Error searching users</div>';
            resultsContainer.style.display = 'block';
        }
    }
}

// Toggle like on a post
async function toggleLike(postId, element) {
    try {
        const isLiked = element.classList.contains('fas');
        
        const response = await fetchWithAuth(`/api/v1/like/${isLiked ? 'unlike' : 'like'}/${postId}`, {
            method: 'POST'
        });
        
        if (!response.ok) {
            throw new Error('Failed to update like');
        }
        
        // Toggle UI
        element.classList.toggle('fas');
        element.classList.toggle('far');
        element.classList.toggle('text-danger');
        
        // Update like count
        const likesElement = element.closest('.post')?.querySelector('.post-likes');
        if (likesElement) {
            const currentLikes = parseInt(likesElement.textContent) || 0;
            const newLikes = isLiked ? currentLikes - 1 : currentLikes + 1;
            likesElement.textContent = `${newLikes} like${newLikes !== 1 ? 's' : ''}`;
        }
        
    } catch (error) {
        console.error('Error toggling like:', error);
    }
}

// Follow/Unfollow user
async function followUser(userId, button) {
    try {
        const isFollowing = button.classList.contains('following');
        
        const response = await fetchWithAuth(`/api/v1/follow/${userId}`, {
            method: isFollowing ? 'DELETE' : 'POST'
        });
        
        if (!response.ok) {
            throw new Error('Failed to update follow status');
        }
        
        // Toggle UI
        button.classList.toggle('following');
        button.textContent = isFollowing ? 'Follow' : 'Following';
        
        // Show success message
        Swal.fire({
            icon: 'success',
            title: isFollowing ? 'Unfollowed' : 'Followed',
            showConfirmButton: false,
            timer: 1500
        });
        
    } catch (error) {
        console.error('Error following user:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Failed to update follow status. Please try again.'
        });
    }
}

// Post a comment
async function postComment(postId, button) {
    const input = button?.previousElementSibling;
    if (!input) return;
    
    const comment = input.value.trim();
    
    if (!comment) {
        return;
    }
    
    try {
        const response = await fetchWithAuth(`/api/v1/comment/${postId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ content: comment })
        });
        
        if (!response.ok) {
            throw new Error('Failed to post comment');
        }
        
        // Clear input
        input.value = '';
        if (button) {
            button.disabled = true;
        }
        
        // Update comment count
        const commentsElement = input.closest('.post')?.querySelector('.post-comments');
        if (commentsElement) {
            const commentCount = parseInt(commentsElement.textContent.match(/\d+/) || 0);
            commentsElement.textContent = `View all ${commentCount + 1} comments`;
            commentsElement.style.display = 'block';
        }
        
    } catch (error) {
        console.error('Error posting comment:', error);
    }
}

// Utility function for debouncing
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// View a story
function viewStory(userId) {
    // Implement story viewing functionality
    console.log('Viewing story for user:', userId);
}

// Focus comment input
function focusComment(postId) {
    const postElement = document.querySelector(`[data-post-id="${postId}"]`);
    if (postElement) {
        const commentInput = postElement.querySelector('.comment-input');
        if (commentInput) {
            commentInput.focus();
        }
    }
}

// Handle comment input keyup
function handleCommentKeyup(event, postId) {
    if (event.key === 'Enter') {
        const button = event.target.nextElementSibling;
        if (button && button.classList.contains('post-btn')) {
            postComment(postId, button);
        }
    } else {
        // Enable/disable post button based on input
        const button = event.target.nextElementSibling;
        if (button && button.classList.contains('post-btn')) {
            button.disabled = !event.target.value.trim();
        }
    }
}

// View comments for a post
function viewComments(postId) {
    // Implement comment viewing functionality
    console.log('Viewing comments for post:', postId);
}
