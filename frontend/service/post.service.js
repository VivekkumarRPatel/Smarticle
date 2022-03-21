import { serverUrl } from "helpers/api";

const getAll = async (token) => {
  let res;
  if (token) {
    res = await (
      await fetch(`${serverUrl}/article/retrieveArticle?visibility=ALL`, {
        method: "GET",
        headers: {
          "jwt-token": `${token}`
        }
      })
    ).json();
  } else {
    res = await (
      await fetch(`${serverUrl}/article/retrieveArticle?visibility=1`, {
        method: "GET",
        headers: {
          "jwt-token": `${token}`
        }
      })
    ).json();
  }
  return res;
};

const post = async (post, token) => {
  const res = await (
    await fetch(`${serverUrl}/article/postarticle`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "jwt-token": `${token}`
      },
      body: JSON.stringify(post),
    })
  ).json();
  if (res["statusCode"] !== 200) throw new Error("Error in posting");
  return res;
}

const getById = async (token, id) => {
  let res;
  try {
    res = await (
      await fetch(`${serverUrl}/article/getArticleById?id=${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "jwt-token": `${token}`
        },
      })
    ).json();
  } catch(err) {
    throw new Error ("Error in fetching");
  }
  // returns an array of 1 element
  return res[0];
}

export const postService = {
  post,
  getAll,
  getById
};